package com.example.exe101_bioverse.auth.dto.response;

import com.example.exe101_bioverse.common.response.PageResponse;
import com.example.exe101_bioverse.model.dto.response.ModelDetailResponse;
import com.example.exe101_bioverse.storage.dto.ModelAssetResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDeskResponse {
    private PageResponse<ModelDetailResponse> models;
    private PageResponse<UserResponse> users;
    private UserCensusResponse census;
    private List<ModelAssetResponse> r2Assets;
    private boolean r2Ok;
    private String modelsError;
    private String usersError;
    private String r2Error;
}
