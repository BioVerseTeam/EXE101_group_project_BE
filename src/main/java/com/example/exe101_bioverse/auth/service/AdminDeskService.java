package com.example.exe101_bioverse.auth.service;

import com.example.exe101_bioverse.auth.dto.response.AdminDeskResponse;
import com.example.exe101_bioverse.auth.dto.response.UserCensusResponse;
import com.example.exe101_bioverse.auth.dto.response.UserResponse;
import com.example.exe101_bioverse.auth.enums.UserStatus;
import com.example.exe101_bioverse.auth.repository.UserRepository;
import com.example.exe101_bioverse.common.response.PageResponse;
import com.example.exe101_bioverse.model.dto.response.ModelDetailResponse;
import com.example.exe101_bioverse.model.service.AdminBioModelService;
import com.example.exe101_bioverse.storage.dto.ModelAssetResponse;
import com.example.exe101_bioverse.storage.service.R2StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminDeskService {

    private static final Logger log = LoggerFactory.getLogger(AdminDeskService.class);
    private static final ZoneId VN_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");

    private final AdminUserService adminUserService;
    private final AdminBioModelService adminBioModelService;
    private final R2StorageService r2StorageService;
    private final UserRepository userRepository;

    public AdminDeskService(
            AdminUserService adminUserService,
            AdminBioModelService adminBioModelService,
            R2StorageService r2StorageService,
            UserRepository userRepository
    ) {
        this.adminUserService = adminUserService;
        this.adminBioModelService = adminBioModelService;
        this.r2StorageService = r2StorageService;
        this.userRepository = userRepository;
    }

    public AdminDeskResponse load() {
        PageResponse<ModelDetailResponse> models = emptyModels();
        PageResponse<UserResponse> users = emptyUsers();
        List<ModelAssetResponse> r2Assets = List.of();
        boolean r2Ok = true;
        String modelsError = null;
        String usersError = null;
        String r2Error = null;

        try {
            models = adminBioModelService.listModels(null, null, null, 0, 20);
        } catch (RuntimeException ex) {
            log.error("Admin desk failed to load models: {}", ex.getMessage());
            modelsError = "Không tải được nhãn mẫu";
        }

        try {
            users = adminUserService.listUsers(
                    null,
                    null,
                    null,
                    PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "createdAt"))
            );
        } catch (RuntimeException ex) {
            log.error("Admin desk failed to load users: {}", ex.getMessage());
            usersError = "Không tải được tài khoản";
        }

        try {
            r2Assets = r2StorageService.listModels();
        } catch (RuntimeException ex) {
            log.error("Admin desk failed to list R2: {}", ex.getMessage());
            r2Ok = false;
            r2Error = "Không tải được file R2";
            r2Assets = List.of();
        }

        UserCensusResponse census = null;
        if (usersError == null) {
            try {
                census = loadCensus();
            } catch (RuntimeException ex) {
                log.error("Admin desk failed to load user census: {}", ex.getMessage());
            }
        }

        return AdminDeskResponse.builder()
                .models(models)
                .users(users)
                .census(census)
                .r2Assets(r2Assets)
                .r2Ok(r2Ok)
                .modelsError(modelsError)
                .usersError(usersError)
                .r2Error(r2Error)
                .build();
    }

    private UserCensusResponse loadCensus() {
        LocalDate today = LocalDate.now(VN_ZONE);
        LocalDateTime weekStart = today.minusDays(7).atStartOfDay();
        LocalDateTime monthStart = today.withDayOfMonth(1).atStartOfDay();

        long total = userRepository.count();
        long students = userRepository.countByRole_Code("STUDENT");
        long active = userRepository.countByStatus(UserStatus.ACTIVE);
        Map<String, Long> byMonth = new HashMap<>();
        for (Object[] row : userRepository.countCreatedByMonth()) {
            if (row == null || row.length < 2 || row[0] == null) continue;
            byMonth.put(String.valueOf(row[0]), toLong(row[1]));
        }

        YearMonth cursor = YearMonth.from(today).minusMonths(11);
        List<UserCensusResponse.MonthBucket> months = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            String key = cursor.toString();
            months.add(UserCensusResponse.MonthBucket.builder()
                    .year(cursor.getYear())
                    .month(cursor.getMonthValue())
                    .label("Thg " + cursor.getMonthValue())
                    .count(byMonth.getOrDefault(key, 0L))
                    .build());
            cursor = cursor.plusMonths(1);
        }

        return UserCensusResponse.builder()
                .total(total)
                .students(students)
                .locked(Math.max(0, total - active))
                .newThisWeek(userRepository.countByCreatedAtGreaterThanEqual(weekStart))
                .newThisMonth(userRepository.countByCreatedAtGreaterThanEqual(monthStart))
                .months(months)
                .build();
    }

    private static long toLong(Object value) {
        if (value instanceof Number number) return number.longValue();
        try {
            return Long.parseLong(String.valueOf(value));
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    private static PageResponse<ModelDetailResponse> emptyModels() {
        return PageResponse.<ModelDetailResponse>builder()
                .items(List.of())
                .totalElements(0)
                .totalPages(0)
                .page(0)
                .size(20)
                .build();
    }

    private static PageResponse<UserResponse> emptyUsers() {
        return PageResponse.<UserResponse>builder()
                .items(List.of())
                .totalElements(0)
                .totalPages(0)
                .page(0)
                .size(20)
                .build();
    }
}
