package com.example.speakingtimer.model

data class Result(
    val menCount: Int,
    val womenCount: Int,
    val womenTime: Long,
    val menTime: Long,
    val percentMenCount: Int,
    val percentWomenCount: Int,
    val percentMenTime: Int,
    val percentWomenTime: Int
)