package com.example.munchkin

import com.google.gson.Gson

data class Jogador(var nome: String,
                   var level: Int,
                   var bonusEquipamento: Int,
                   var modificadores: Int,
                   var poder: Int)

{
    companion object{
        private val gson = Gson()


        fun fromJson(json: String?): Jogador{
            return gson.fromJson(json, Jogador::class.java)
        }

        fun toJson(jogador: Jogador):String{
            return gson.toJson(Jogador)
        }
    }


}