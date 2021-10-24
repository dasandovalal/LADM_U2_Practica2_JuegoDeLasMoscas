package mx.tecnm.tepic.ladm_u2_practica2_juegomoscas

import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.CountDownTimer
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast

class Lienzo(p:MainActivity):View(p) {
    val principal= p
    var aplastada = BitmapFactory.decodeResource(principal.resources,R.drawable.mosquidead)
    val cantidadMoscas : Int = ((Math.random()*21)+80).toInt()
    val moscas = ArrayList<Mosca>()
    var puntaje = 0
    var temporizador = ""
    var timer =  object : CountDownTimer(60000,10){
        override fun onTick(p0: Long) {
            temporizador = "Tiempo restante: ${p0/1000} Segundos"
            if(puntaje==cantidadMoscas){
                this.cancel()
                AlertDialog.Builder(principal)
                    .setTitle("GANASTE")
                    .setMessage("ACABASTE CON LA RAZA MOSCA")
                    .setPositiveButton("CERRAR APP"){dialog,exception ->
                        dialog.cancel()
                        principal.finish()
                    }.show()
            }
            if(p0/1000L==0.toLong()) {
                this.cancel()
                AlertDialog.Builder(principal)
                    .setTitle("PERDISTE")
                    .setMessage("ERES ESCLAVO DE LAS MOSCAS")
                    .setPositiveButton("CERRAR APP"){dialog,exception ->
                        dialog.cancel()
                        principal.finish()
                    }.show()
            }
        }

        override fun onFinish() {
            this.cancel()
        }
    }

    init{
        timer.start()
        val movimientoMosca = MovimientoMosca(this,cantidadMoscas)
        movimientoMosca.start()
    }

    override fun onDraw(c: Canvas) {
        super.onDraw(c)
        val p = Paint()

        c.drawColor(Color.LTGRAY)

        (0 until moscas.size).forEach {
            moscas[it].pintar(c,p)
        }

        p.textSize = 80f
        p.setColor(Color.RED)
        c.drawText("Moscas muertas: "+puntaje.toString() + " de " + "${cantidadMoscas}",75f,125f,p)

        p.textSize = 90f
        c.drawText(""+temporizador,75f,225f,p)

    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.action){
            MotionEvent.ACTION_DOWN->{
                var victima = 0
                try {
                    while (victima <= moscas.size) {
                        if (moscas[victima].estaEnArea(event.x, event.y)) {
                            if (moscas[victima].vivita) {
                                puntaje++
                                moscas[victima].mosquita = aplastada
                                moscas[victima].vivita = false
                            }
                        }
                        victima++
                    }
                }catch (io:Exception){
                    Log.d("HUBO PROBLEMA","${io.message}")

                }

            }
        }
        invalidate()
        return true
    }


}

class MovimientoMosca(p:Lienzo,c:Int):Thread(){
    val p = p

    init {
        (1..c).forEach {
            val mosca = Mosca(p)
            p.moscas.add(mosca)
        }
    }

    override fun run() {
        super.run()
        while (true){
            (0 until p.moscas.size).forEach{
              p.moscas[it].moverMosca()
            }
            p.principal.runOnUiThread(){
                p.invalidate()
            }
            sleep(100)
        }
    }
}