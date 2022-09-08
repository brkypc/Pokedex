package com.example.challenge3.feature.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.challenge3.R
import com.example.challenge3.data.local.Pokemon
import com.example.challenge3.data.remote.responses.AbilityParent
import com.example.challenge3.data.remote.responses.TypeParent
import com.example.challenge3.databinding.FragmentPokemonDetailBinding
import com.example.challenge3.utils.Constants.ARG_PARAM1
import com.example.challenge3.utils.Constants.animDuration
import com.example.challenge3.utils.ProgressAnimation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PokemonDetailFragment : Fragment() {
    private val viewModel: PokemonDetailViewModel by viewModels()
    private lateinit var binding: FragmentPokemonDetailBinding
    private var param1: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPokemonDetailBinding.inflate(inflater, container, false)

        viewModel.pokemonLiveData.observe(viewLifecycleOwner) {
            setData(it)
        }

        viewModel.getPokemonFromDatabase(param1)
        observeInternetStatus()
        defineListeners()

        return binding.root
    }

    private fun observeInternetStatus() {
        viewModel.internetStatusLiveData.observe(viewLifecycleOwner) {
            when (it) {
                1 -> binding.progressBar.visibility = View.VISIBLE
                2 -> createAlertDialog()
            }
        }
    }

    private fun setData(pokemon: Pokemon) {
        binding.progressBar.visibility = View.GONE
        binding.constraintLayout.visibility = View.VISIBLE

        if (pokemon.imageURL.isNotEmpty())
            Glide.with(requireContext()).load(pokemon.imageURL).into(binding.pokemonImage)
        else
            binding.pokemonImage.setImageResource(R.drawable.poke_ball)

        binding.collapsingToolbar.title = "#".plus(pokemon.poke_id).plus(" ")
            .plus(pokemon.name.replaceFirstChar { it.uppercase() })
        binding.weight.text = pokemon.weight?.toString()?.plus(" kg")
        binding.height.text = pokemon.height?.toString()?.plus(" m")
        buildPokemonDetailText(pokemon.abilities, pokemon.types)

        pokemon.stats?.let {
            binding.apply {
                hpProgressBar.progress = it[0].baseStat.also { hp.text = it.toString() }
                atkProgressBar.progress = it[1].baseStat.also { atk.text = it.toString() }
                defProgressBar.progress = it[2].baseStat.also { def.text = it.toString() }
                spAtkProgressBar.progress = it[3].baseStat.also { spAtk.text = it.toString() }
                spDefProgressBar.progress = it[4].baseStat.also { spDef.text = it.toString() }
                speedProgressBar.progress = it[5].baseStat.also { speed.text = it.toString() }
            }
            createAnimations()
        }
    }

    private fun createAnimations() {
        binding.run {
            val anim1 = ProgressAnimation(hpProgressBar, hpProgressBar.progress).also {
                it.duration = animDuration
            }
            val anim2 = ProgressAnimation(atkProgressBar, atkProgressBar.progress).also {
                it.duration = animDuration
            }
            val anim3 = ProgressAnimation(defProgressBar, defProgressBar.progress).also {
                it.duration = animDuration
            }
            val anim4 = ProgressAnimation(spAtkProgressBar, spAtkProgressBar.progress).also {
                it.duration = animDuration
            }
            val anim5 = ProgressAnimation(spDefProgressBar, spDefProgressBar.progress).also {
                it.duration = animDuration
            }
            val anim6 = ProgressAnimation(speedProgressBar, speedProgressBar.progress).also {
                it.duration = animDuration
            }

            hpProgressBar.startAnimation(anim1)
            atkProgressBar.startAnimation(anim2)
            defProgressBar.startAnimation(anim3)
            spAtkProgressBar.startAnimation(anim4)
            spDefProgressBar.startAnimation(anim5)
            speedProgressBar.startAnimation(anim6)
        }
    }

    private fun buildPokemonDetailText(abilitiesParent: List<AbilityParent>?, typesParent: List<TypeParent>?) {
        abilitiesParent?.let {
            val abilityString = StringBuilder()

            for (abilityParent in it)
                abilityString.append(abilityParent.ability.name).append("\n")

            binding.abilities.text = abilityString.toString()
        }
        typesParent?.let {
            val typesString = StringBuilder()

            for (typeParent in it)
                typesString.append(typeParent.type.name).append("\n")

            binding.types.text = typesString.toString()
        }
    }

    private fun defineListeners() {
        binding.fab.setOnClickListener {
            binding.appBar.setExpanded(false)
        }
    }

    private fun createAlertDialog() {
        AlertDialog.Builder(requireContext())
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle(getString(R.string.connection_error))
            .setMessage(getString(R.string.first_pokemon_internet))
            .setPositiveButton(getString(R.string.retry)) { _, _ ->
                viewModel.getPokemonFromDatabase(param1)
            }
            .setNegativeButton(getString(R.string.back)) { _, _ -> requireActivity().onBackPressed() }
            .setCancelable(false)
            .show()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int) =
            PokemonDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                }
            }
    }
}