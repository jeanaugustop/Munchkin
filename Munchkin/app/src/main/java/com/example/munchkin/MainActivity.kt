package com.example.munchkin

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LayoutPreview()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LayoutPreview() {
    LayoutNav()
}

@Composable
fun LayoutNav() {

    val navController = rememberNavController()
    val listaJogadores = remember { mutableStateListOf<Jogador>()}

    // NavHost define as rotas e o ponto de entrada
        NavHost(navController = navController, startDestination = "LayoutCadastro") {
        composable("LayoutCadastro") { LayoutCadastro(navController, listaJogadores) }
        composable("LayoutLista") { LayoutLista(navController, listaJogadores) }
        composable("LayoutDetalhes/{jogadorJson}") { backStackEntry ->
             val jogadorJson = backStackEntry.arguments?.getString("jogadorJson")
             val jogadorResp = Jogador.fromJson(jogadorJson)

             LayoutDetalhes(navController, jogadorResp, listaJogadores)
            }

    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LayoutCadastro(navController: NavHostController, list: MutableList<Jogador>) {

    var nome by remember { mutableStateOf("") }
    var context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally)
    {

        Text(text = "INFORME SEU NOME:")

        Spacer(modifier = Modifier.height(20.dp))

        TextField(value = nome,
            onValueChange = {nome = it},
            label = { Text(text = "Nome do Jogador:")})

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            if(nome.isEmpty()){
                Toast.makeText(context, "Preencha o Campo", Toast.LENGTH_LONG).show()
            }
            var jogador = Jogador(nome, 0,0,0, 0)
            list.add(jogador)
            navController.navigate("LayoutLista")

        }){

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Cadastrar")

        }

    }

}

@Composable
fun LayoutLista(navController: NavHostController, listaJogadores: List<Jogador>) {

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

        LazyColumn{
            items(listaJogadores){

                jogador ->

                Button(onClick = {
                    val jogadorJson = Jogador.toJson(jogador)

                    navController.navigate("LayoutDetalhes/$jogadorJson")
                }) {

                    Text(text = "Jogador: ${jogador.nome}")

                }
            }
        }

    }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally)
    {
        Button(onClick = {
            navController.navigate("LayoutCadastro")
        }) {
            Text(text = "Voltar ao cadastro")
        }
    }



}

@Composable
fun LayoutDetalhes(
    navController: NavHostController,
    jogador: Jogador,
    listaJogadores: List<Jogador>
) {

    val nomeJogador = navController.previousBackStackEntry?.arguments?.getString("nome") // Recupera o nome do argumento

    // Variáveis de estado para os contadores
    var level by remember { mutableStateOf(0) }
    var equipamento by remember { mutableStateOf(0) }
    var modificador by remember { mutableStateOf(5) }
    var poder = level + equipamento + modificador

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Jogador: ${jogador.nome}\n Poder: ${jogador.poder}}")

        Spacer(modifier = Modifier.height(20.dp))

        // Contador de Level
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = {
                if (level > 0) level -= 1  // Decrementa o level com limite mínimo de 0
            }) {
                Text(text = "-")
            }

            Spacer(modifier = Modifier.width(20.dp))

            Text(text = "Level: $level")

            Spacer(modifier = Modifier.width(20.dp))

            Button(onClick = {
                if (level < 10) level += 1  // Incrementa o level com limite máximo de 10
            }) {
                Text(text = "+")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Contador de Equipamento
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = {
                if (equipamento > 0) equipamento -= 1  // Decrementa o equipamento com limite mínimo de 0
            }) {
                Text(text = "-")
            }

            Spacer(modifier = Modifier.width(20.dp))

            Text(text = "Equipamento: $equipamento")

            Spacer(modifier = Modifier.width(20.dp))

            Button(onClick = {
                if (equipamento < 40) equipamento += 1  // Incrementa o equipamento com limite máximo de 40
            }) {
                Text(text = "+")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Contador de Modificador
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = {
                if (modificador > -5) modificador -= 1  // Decrementa o modificador com limite mínimo de 5
            }) {
                Text(text = "-")
            }

            Spacer(modifier = Modifier.width(20.dp))

            Text(text = "Modificador: $modificador")

            Spacer(modifier = Modifier.width(20.dp))

            Button(onClick = {
                if (modificador < 10) modificador += 1  // Incrementa o modificador com limite máximo de 10
            }) {
                Text(text = "+")
            }
        }
    }
    Column {
        Button(onClick = {navController.popBackStack()}) {
            
            Text(text = "Voltar")
            
        }
    }
}