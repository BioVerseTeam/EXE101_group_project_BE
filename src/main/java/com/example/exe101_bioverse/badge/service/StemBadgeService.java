package com.example.exe101_bioverse.badge.service;

import com.example.exe101_bioverse.badge.dto.request.CreateBadgeRequest;
import com.example.exe101_bioverse.badge.dto.request.UpdateBadgeRequest;
import com.example.exe101_bioverse.badge.dto.response.BadgeResponse;

import java.util.List;

public interface StemBadgeService {

    List<BadgeResponse> listPublic();

    List<BadgeResponse> listAdmin();

    BadgeResponse getById(Long id);

    BadgeResponse create(CreateBadgeRequest request);

    BadgeResponse update(Long id, UpdateBadgeRequest request);

    void delete(Long id);

    BadgeResponse toggleStatus(Long id);
}
