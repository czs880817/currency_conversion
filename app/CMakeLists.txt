cmake_minimum_required(VERSION 3.4.1)

add_library(
        AccessKey
        SHARED
        src/main/jni/access_key.c
)

include_directories(src/main/jni)

find_library(
        log-lib
        log
)

target_link_libraries(
        AccessKey
        ${log-lib}
)