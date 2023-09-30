package com.kitapacak.wecan.Response

import com.google.gson.annotations.SerializedName

data class KodeKotaResponse(
	@field:SerializedName("kota")
	val kota: List<KotaItem?>? = null
)

data class KotaItem(
	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)
