package br.edu.ifsp.scl.sc303769x.pedrapapeltesoura

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.edu.ifsp.scl.sc303769x.pedrapapeltesoura.ui.theme.PedraPapelTesouraTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PedraPapelTesouraTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    JoKenPoScreen()
                }
            }
        }
    }
}

enum class Jogada(val icone: String, val descricao: String) {
    PEDRA("🪨", "Pedra"),
    PAPEL("📄", "Papel"),
    TESOURA("✂️", "Tesoura"),
    NENHUMA("❓", "Aguardando...")
}

@Composable
fun JoKenPoScreen() {
    var jogadaUsuario by remember { mutableStateOf(Jogada.NENHUMA) }
    var jogadaApp by remember { mutableStateOf(Jogada.NENHUMA) }
    var resultado by remember { mutableStateOf("Faça sua jogada!") }

    fun jogarRodada(escolhaDoUsuario: Jogada) {
        jogadaUsuario = escolhaDoUsuario

        val opcoes = listOf(Jogada.PEDRA, Jogada.PAPEL, Jogada.TESOURA)
        jogadaApp = opcoes[Random.nextInt(opcoes.size)]

        resultado = when {
            jogadaUsuario == jogadaApp -> "Empate!"
            jogadaUsuario == Jogada.PEDRA && jogadaApp == Jogada.TESOURA -> "Você Venceu!"
            jogadaUsuario == Jogada.PAPEL && jogadaApp == Jogada.PEDRA -> "Você Venceu!"
            jogadaUsuario == Jogada.TESOURA && jogadaApp == Jogada.PAPEL -> "Você Venceu!"
            else -> "Você Perdeu!"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(text = "JoKenPo", fontSize = 36.sp, fontWeight = FontWeight.Bold)

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Jogada do App:", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            Text(text = jogadaApp.icone, fontSize = 80.sp, modifier = Modifier.padding(16.dp))
            Text(text = jogadaApp.descricao, fontSize = 18.sp)
        }

        Text(
            text = resultado,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = when (resultado) {
                "Você Venceu!" -> MaterialTheme.colorScheme.primary
                "Você Perdeu!" -> MaterialTheme.colorScheme.error
                else -> MaterialTheme.colorScheme.onBackground
            }
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Escolha sua arma:",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                BotaoJogada(jogada = Jogada.PEDRA, onClick = { jogarRodada(Jogada.PEDRA) })
                BotaoJogada(jogada = Jogada.PAPEL, onClick = { jogarRodada(Jogada.PAPEL) })
                BotaoJogada(jogada = Jogada.TESOURA, onClick = { jogarRodada(Jogada.TESOURA) })
            }
        }
    }
}

@Composable
fun BotaoJogada(jogada: Jogada, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.size(100.dp),
        shape = MaterialTheme.shapes.medium,
        contentPadding = PaddingValues(8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = jogada.icone, fontSize = 36.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = jogada.descricao, fontSize = 14.sp)
        }
    }
}