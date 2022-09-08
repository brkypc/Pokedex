package com.example.challenge3.feature.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.challenge3.R
import com.example.challenge3.data.local.Pokemon
import com.example.challenge3.databinding.FragmentPokemonListBinding
import com.example.challenge3.feature.detail.PokemonDetailFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PokemonListFragment : Fragment() {
    private val viewModel: PokemonListViewModel by viewModels()
    private lateinit var binding: FragmentPokemonListBinding
    private var pokeID = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPokemonListBinding.inflate(inflater, container, false)

        viewModel.pokemonLiveData.observe(viewLifecycleOwner) {
            defineRecyclerView(it)
        }

        viewModel.getPokemonsFromDatabase()
        observeInternetStatus()
        defineListeners()

        return binding.root
    }

    private fun observeInternetStatus() {
        viewModel.internetStatusLiveData.observe(viewLifecycleOwner) {
            when (it) {
                1 -> {
                    binding.rvPokemons.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                }
                2 -> {
                    if (pokeID != 0) createAlertDialog(1)
                    else createAlertDialog(2)
                }
            }
        }
    }

    private fun defineListeners() {
        binding.previousPage.setOnClickListener { viewModel.onPreviousClick() }
        binding.nextPage.setOnClickListener { viewModel.onNextClick() }

        val mAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            IntRange(1, 58).toList()
        )
        //mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinner.apply {
            adapter = mAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.onPageClick(mAdapter.getItem(position) ?: 1)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        }
    }

    private fun defineRecyclerView(pokemons: ArrayList<Pokemon>) {
        pokeID = pokemons[0].poke_id
        when (pokeID) {
            1 -> {
                binding.previousPage.visibility = View.INVISIBLE
                binding.nextPage.visibility = View.VISIBLE
            }
            10236 -> {
                binding.nextPage.visibility = View.INVISIBLE
                binding.previousPage.visibility = View.VISIBLE
            }
            else -> {
                binding.previousPage.visibility = View.VISIBLE
                binding.nextPage.visibility = View.VISIBLE
            }
        }

        binding.progressBar.visibility = View.GONE
        binding.rvPokemons.visibility = View.VISIBLE

        binding.rvPokemons.layoutManager = GridLayoutManager(requireContext(), 2)

        val pokemonAdapter = PokemonAdapter(requireContext(), pokemons)
        binding.rvPokemons.adapter = pokemonAdapter

        pokemonAdapter.onPokemonClick = { pokemon ->
            requireActivity().supportFragmentManager
                .beginTransaction()
                .add(
                    R.id.fragment_container,
                    PokemonDetailFragment.newInstance(pokemon.poke_id)
                )
                .addToBackStack("detail")
                .commit()
        }
    }

    private fun createAlertDialog(select : Int) {
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle(getString(R.string.connection_error))
            .setCancelable(false)

        when(select) {
            1 -> alertDialog.setMessage(getString(R.string.first_page_internet))
                    .setPositiveButton(getString(R.string.retry)) { _, _ -> viewModel.onNextClick() }
                    .setNegativeButton(getString(R.string.close)) { _, _ -> }

            2 -> alertDialog.setMessage(getString(R.string.first_run_internet))
                    .setPositiveButton(getString(R.string.retry)) { _, _ -> viewModel.getPokemonsFromInternet() }

        }
        alertDialog.show()
    }
}