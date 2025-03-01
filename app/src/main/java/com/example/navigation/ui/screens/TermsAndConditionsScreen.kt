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
fun TermsAndConditionsScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Términos y Condiciones",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        Text(
            text = "Fecha de última actualización: 1 de marzo de 2025\n\n" +
                    "1. Aceptación de los Términos\n" +
                    "Al utilizar nuestra aplicación, usted acepta cumplir con estos Términos y Condiciones. Si no está de acuerdo con alguno de los términos, le recomendamos no utilizar la aplicación.\n\n" +
                    "2. Uso de la Aplicación\n" +
                    "Nuestra aplicación está destinada a usuarios mayores de 13 años. Usted se compromete a usar la aplicación únicamente con fines lícitos y de acuerdo con estos términos.\n\n" +
                    "3. Registro y Seguridad de la Cuenta\n" +
                    "Para acceder a ciertas funciones de la aplicación, se le puede pedir que cree una cuenta. Usted es responsable de mantener la confidencialidad de sus credenciales de inicio de sesión y de todas las actividades realizadas en su cuenta. Si sospecha de cualquier uso no autorizado de su cuenta, debe notificarnos de inmediato.\n\n" +
                    "4. Propiedad Intelectual\n" +
                    "Todo el contenido, diseño y tecnología de la aplicación, incluyendo pero no limitado a gráficos, texto, logos y software, es propiedad de [Nombre de la Empresa] y está protegido por leyes de propiedad intelectual. No puede copiar, modificar o distribuir este contenido sin nuestra autorización previa por escrito.\n\n" +
                    "5. Limitación de Responsabilidad\n" +
                    "No nos hacemos responsables de ningún daño o pérdida de datos, ni de ningún perjuicio directo o indirecto derivado del uso de la aplicación. El uso de la aplicación es bajo su propio riesgo.\n\n" +
                    "6. Modificaciones\n" +
                    "Nos reservamos el derecho de modificar estos términos en cualquier momento. Cualquier cambio será notificado a través de la aplicación y estará disponible en esta página. El uso continuado de la aplicación tras dichos cambios constituye su aceptación de los nuevos términos.\n\n" +
                    "7. Terminación\n" +
                    "Podemos suspender o finalizar su acceso a la aplicación si violara estos términos, sin previo aviso. Usted puede cancelar su cuenta en cualquier momento.\n\n" +
                    "8. Ley Aplicable\n" +
                    "Estos términos se regirán e interpretarán de acuerdo con las leyes del [país o región]. Cualquier disputa será resuelta en los tribunales competentes de dicha jurisdicción.",
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
