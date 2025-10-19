package com.example.pokedex.ui.lutar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.pokedex.databinding.FragmentLutarEquipeRocketBinding
import kotlin.random.Random

class LutarEquipeRocket : Fragment() {
    private var binding: FragmentLutarEquipeRocketBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLutarEquipeRocketBinding.inflate(inflater, container, false)

        Glide.with(this)
            .asGif()
            .load("https://c.tenor.com/OUb7BSTONNsAAAAC/tenor.gif")
            .into(binding!!.gifEquipeRocket)

        binding?.btAtacar?.setOnClickListener {
            val numero = Random.nextInt(1, 101)

            if (numero % 2 == 0) {
                binding?.textoSalvar?.text = "Você salvou os pokémons das garras da Equipe Rocket!"
                Glide.with(this)
                    .asGif()
                    .load("https://c.tenor.com/9OTugC1NH-gAAAAC/tenor.gif")
                    .into(binding!!.gifEquipeRocket)
            } else {
                binding?.textoSalvar?.text = "Você não conseguiu salvar os pokémons..."
                Glide.with(this)
                    .asGif()
                    .load("https://c.tenor.com/GcMFPo-W7ZkAAAAC/tenor.gif")
                    .into(binding!!.gifEquipeRocket)
            }

            binding!!.textoSalvar.visibility = View.VISIBLE
        }

        return binding!!.root
    }

}