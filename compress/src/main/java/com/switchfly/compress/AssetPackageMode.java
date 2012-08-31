package com.switchfly.compress;

/**
 * Can be either development, debug, or production
 * * development: no packaging, no compression
 * * debug: packaging, no compression
 * * production: packaging and compression
 */
public enum AssetPackageMode {
    DEVELOPMENT,
    DEBUG,
    PRODUCTION
}