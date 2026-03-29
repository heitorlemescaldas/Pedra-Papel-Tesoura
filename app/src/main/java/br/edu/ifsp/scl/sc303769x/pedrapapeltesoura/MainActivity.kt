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
    PEDRA("✊", "Pedra"),
    PAPEL("📄", "Papel"),
    TESOURA("✂️", "Tesoura"),
    NENHUMA("❓", "Aguardando...")
}

@Composable
fun JoKenPoScreen() {
    // Estados do Jogo
    var qtdJogadores by remember { mutableIntStateOf(2) }
    var jogadaUsuario by remember { mutableStateOf(Jogada.NENHUMA) }
    var jogadaApp1 by remember { mutableStateOf(Jogada.NENHUMA) }
    var jogadaApp2 by remember { mutableStateOf(Jogada.NENHUMA) } // Usado apenas no modo 3 jogadores
    var resultado by remember { mutableStateOf("Faça sua jogada!") }

    // Função auxiliar para resetar a mesa ao trocar de modo
    fun resetarMesa(jogadores: Int) {
        qtdJogadores = jogadores
        jogadaUsuario = Jogada.NENHUMA
        jogadaApp1 = Jogada.NENHUMA
        jogadaApp2 = Jogada.NENHUMA
        resultado = "Faça sua jogada!"
    }

    // Regras Clássicas para 2 Jogadores
    fun calcularVencedor2Jogadores(user: Jogada, app: Jogada): String {
        return when {
            user == app -> "Empate!"
            user == Jogada.PEDRA && app == Jogada.TESOURA -> "Você Venceu!"
            user == Jogada.PAPEL && app == Jogada.PEDRA -> "Você Venceu!"
            user == Jogada.TESOURA && app == Jogada.PAPEL -> "Você Venceu!"
            else -> "Você Perdeu!"
        }
    }

    // Regras Complexas para 3 Jogadores usando Conjuntos (Set)
    fun calcularVencedor3Jogadores(user: Jogada, app1: Jogada, app2: Jogada): String {
        val jogadasNaMesa = setOf(user, app1, app2)

        return when (jogadasNaMesa.size) {
            1 -> "Empate! Todos iguais." // Ex: Pedra, Pedra, Pedra
            3 -> "Empate! Tudo na mesa." // Ex: Pedra, Papel, Tesoura
            else -> {
                // Se o tamanho do Set é 2, significa que saíram apenas duas opções diferentes na mesa.
                // Precisamos descobrir qual das duas é a mais forte.
                val opcoes = jogadasNaMesa.toList()
                val opcaoA = opcoes[0]
                val opcaoB = opcoes[1]

                val jogadaVencedora = when {
                    opcaoA == Jogada.PEDRA && opcaoB == Jogada.TESOURA -> opcaoA
                    opcaoA == Jogada.PAPEL && opcaoB == Jogada.PEDRA -> opcaoA
                    opcaoA == Jogada.TESOURA && opcaoB == Jogada.PAPEL -> opcaoA
                    else -> opcaoB
                }

                // Se a jogada do usuário é igual à jogada mais forte da mesa, ele venceu
                if (user == jogadaVencedora) "Você Venceu!" else "Você Perdeu!"
            }
        }
    }

    // Motor do Jogo
    fun jogarRodada(escolha: Jogada) {
        jogadaUsuario = escolha
        val opcoes = listOf(Jogada.PEDRA, Jogada.PAPEL, Jogada.TESOURA)

        jogadaApp1 = opcoes[Random.nextInt(opcoes.size)]

        if (qtdJogadores == 3) {
            jogadaApp2 = opcoes[Random.nextInt(opcoes.size)]
            resultado = calcularVencedor3Jogadores(jogadaUsuario, jogadaApp1, jogadaApp2)
        } else {
            resultado = calcularVencedor2Jogadores(jogadaUsuario, jogadaApp1)
        }
    }

    // Desenho da Interface
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // --- TOPO: Configuração de Jogadores ---
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "JoKenPo", fontSize = 36.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { resetarMesa(2) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (qtdJogadores == 2) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = if (qtdJogadores == 2) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    Text("2 Jogadores")
                }
                Button(
                    onClick = { resetarMesa(3) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (qtdJogadores == 3) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = if (qtdJogadores == 3) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    Text("3 Jogadores")
                }
            }
        }

        // --- MEIO: Adversários ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "App 1", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                Text(text = jogadaApp1.icone, fontSize = 64.sp, modifier = Modifier.padding(16.dp))
                Text(text = jogadaApp1.descricao, fontSize = 16.sp)
            }

            if (qtdJogadores == 3) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "App 2", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                    Text(text = jogadaApp2.icone, fontSize = 64.sp, modifier = Modifier.padding(16.dp))
                    Text(text = jogadaApp2.descricao, fontSize = 16.sp)
                }
            }
        }

        // --- RESULTADO ---
        Text(
            text = resultado,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = when {
                resultado.contains("Venceu") -> MaterialTheme.colorScheme.primary
                resultado.contains("Perdeu") -> MaterialTheme.colorScheme.error
                else -> MaterialTheme.colorScheme.onBackground
            },
            modifier = Modifier.padding(vertical = 16.dp)
        )

        // --- BASE: Escolha do Usuário ---
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(bottom = 32.dp)) {
            Text(text = "Sua Escolha:", fontSize = 20.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(bottom = 16.dp))
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
        modifier = Modifier.size(90.dp),
        shape = MaterialTheme.shapes.medium,
        contentPadding = PaddingValues(4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = jogada.icone, fontSize = 32.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = jogada.descricao, fontSize = 14.sp)
        }
    }
}