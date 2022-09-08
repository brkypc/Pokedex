package com.example.challenge3.feature.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.challenge3.R
import com.example.challenge3.databinding.ActivityMainBinding
import com.example.challenge3.feature.list.PokemonListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, PokemonListFragment()).commit()
    }
}