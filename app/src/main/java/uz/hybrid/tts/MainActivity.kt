package uz.hybrid.tts

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private var tts: TextToSpeech? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Oddiy va tezkor ekran interfeysi (XML faylsiz)
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 40, 40, 40)
        }

        val inputEdit = EditText(this).apply {
            hint = "Matn kiriting (ş, sh, ō, o', kirill)..."
            minLines = 4
        }

        val radioGroup = RadioGroup(this).apply {
            orientation = RadioGroup.HORIZONTAL
            addView(RadioButton(context).apply { id = 1; text = "Yangi (ş, ō)"; isChecked = true })
            addView(RadioButton(context).apply { id = 2; text = "Lotin (sh, o')" })
            addView(RadioButton(context).apply { id = 3; text = "Kirill" })
        }

        val outputText = TextView(this).apply {
            textSize = 16f
            setPadding(0, 20, 0, 20)
        }

        val btnSpeak = Button(this).apply { text = "🔊 OʻQISH VA OʻGIRISH" }
        val btnSystemTts = Button(this).apply { text = "⚙️ Telefon TTS sozlamasiga qoʻshish" }

        layout.addView(inputEdit)
        layout.addView(radioGroup)
        layout.addView(btnSpeak)
        layout.addView(outputText)
        layout.addView(btnSystemTts)
        setContentView(layout)

        // Tizim TTS ni ulash
        tts = TextToSpeech(this, { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale("uz", "UZ")
            }
        }, packageName)

        btnSpeak.setOnClickListener {
            val raw = inputEdit.text.toString()
            val mode = when (radioGroup.checkedRadioButtonId) {
                1 -> "yangi"
                2 -> "lotin"
                else -> "kirill"
            }
            outputText.text = UzbekHybridConverter.formatOutput(raw, mode)
            tts?.speak(raw, TextToSpeech.QUEUE_FLUSH, null, "uz_tts_id")
        }

        btnSystemTts.setOnClickListener {
            startActivity(Intent("com.android.settings.TTS_SETTINGS"))
        }
    }

    override fun onDestroy() {
        tts?.shutdown()
        super.onDestroy()
    }
}
