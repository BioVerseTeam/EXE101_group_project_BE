package com.example.exe101_bioverse.exam.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaUploadResponse {
    private String url;
    private String fileName;
    private long fileSize;
    private String mimeType;
    private Integer width;
    private Integer height;
}
