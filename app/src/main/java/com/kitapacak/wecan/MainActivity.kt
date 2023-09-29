package com.kitapacak.wecan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import com.kitapacak.wecan.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnClickListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.isiSaldo.setOnClickListener(this)
        binding.riwayat.setOnClickListener(this)
        binding.donasi.setOnClickListener(this)

        //masukin nilai Duet
        binding.tvCashDonasi.text = duetAnda.toString()
    }

    override fun onClick(view: View?) {
        when(view){
            binding.isiSaldo -> {
                val intent = Intent(this@MainActivity, IsiSaldoActivity::class.java)
                startActivity(intent)
            }
            binding.riwayat -> {
                val intent = Intent(this@MainActivity, RiwayatActivity::class.java)
                startActivity(intent)
            }
            binding.donasi -> {
                val intent = Intent(this@MainActivity, DonasiOtomatisActivity::class.java)
                startActivity(intent)
            }
        }
    }

    companion object {
        private var duetAnda = 10000
    }
}