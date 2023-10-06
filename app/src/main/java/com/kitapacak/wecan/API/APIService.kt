package com.kitapacak.wecan.API

import com.kitapacak.wecan.Response.JadwalSolatResponse
import com.kitapacak.wecan.Response.KodeKotaResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface APIService {
    @GET("jadwal/kota/{kode}/tanggal/{tanggal}")
    fun getJadwal(
        @Path("kode") username: String,
        @Path("tanggal") tanggal: String
    ): Call<JadwalSolatResponse>

    @GET("kota/nama/{kota}")
    fun getCityId(
        @Path("kota") kota: String
    ): Call<KodeKotaResponse>
}