package com.example.navigation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun PrivacyPolicyScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Política de Privacidad",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        Text(
            text = "Fecha de última actualización: 1 de marzo de 2025\n\n" +
                    "1. Información que Recopilamos\n" +
                    "Recopilamos la siguiente información personal para proporcionarle una experiencia mejorada dentro de nuestra aplicación:\n" +
                    "- Información personal: Cuando se registra, puede proporcionarnos su nombre, correo electrónico, y otros datos de contacto.\n" +
                    "- Datos de uso: Recopilamos información sobre cómo interactúa con la aplicación, como el tipo de dispositivo, sistema operativo, y páginas visitadas.\n\n" +
                    "2. Uso de la Información\n" +
                    "Utilizamos la información recopilada para los siguientes fines:\n" +
                    "- Mejorar y personalizar la experiencia del usuario.\n" +
                    "- Enviar actualizaciones y notificaciones relacionadas con la aplicación.\n" +
                    "- Realizar análisis internos para mejorar la funcionalidad de la app.\n" +
                    "- Prevenir fraudes y proteger la seguridad de la cuenta.\n\n" +
                    "3. Compartir Información\n" +
                    "No compartimos su información personal con terceros, excepto en los siguientes casos:\n" +
                    "- Proveedores de servicios: Podemos compartir su información con proveedores de servicios que nos ayudan a operar y mantener la aplicación.\n" +
                    "- Requisitos legales: Si es necesario, podemos divulgar su información para cumplir con obligaciones legales.\n\n" +
                    "4. Seguridad de la Información\n" +
                    "Tomamos medidas razonables para proteger su información personal, incluyendo el uso de tecnologías de encriptación. Sin embargo, no podemos garantizar la seguridad absoluta de sus datos en todo momento.\n\n" +
                    "5. Sus Derechos\n" +
                    "Usted tiene derecho a:\n" +
                    "- Acceder, actualizar o corregir su información personal.\n" +
                    "- Eliminar su cuenta y la información asociada, si lo desea.\n" +
                    "- Solicitar la limitación o restricción del uso de sus datos personales.\n\n" +
                    "6. Cookies y Tecnologías Similares\n" +
                    "Utilizamos cookies para mejorar la experiencia del usuario. Puede configurar su dispositivo para rechazar las cookies, pero esto podría afectar el funcionamiento de algunas características.\n\n" +
                    "7. Enlaces a Sitios de Terceros\n" +
                    "Nuestra aplicación puede contener enlaces a sitios web de terceros. No somos responsables de la privacidad o el contenido de esos sitios.\n\n" +
                    "8. Cambios en la Política de Privacidad\n" +
                    "Podemos actualizar esta Política de Privacidad en cualquier momento. Los cambios serán notificados a través de la aplicación.\n\n" +
                    "9. Contacto\n" +
                    "Si tiene alguna pregunta, contáctenos a través de [correo electrónico o formulario de contacto].",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Volver a la pantalla anterior
        ElevatedButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(text = "Volver", color = Color.White)
        }
    }
}
