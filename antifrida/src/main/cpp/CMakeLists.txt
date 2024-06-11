cmake_minimum_required(VERSION 3.4.1)

add_library(fileops SHARED fileops.c)

find_library(log-lib log)

target_link_libraries(fileops ${log-lib})