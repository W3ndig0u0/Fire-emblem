package com.example.demo.dto;

public record UseItemResult(
        String message,
        int targetNewHealth,
        boolean itemGone
) {}