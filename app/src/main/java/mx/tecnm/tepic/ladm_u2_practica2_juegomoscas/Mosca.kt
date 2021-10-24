package mx.tecnm.tepic.ladm_u2_practica2_juegomoscas

import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint

class Mosca(l:Lienzo) {
    var x = 0f
    var y = 0f
    var vel = 15
    var camino = -1
    var mosquita = BitmapFactory.decodeResource(l.resources,R.drawable.mosquita1)
    var vivita = true

    init {
        x = (Math.random()*2340).toFloat()
        y = (Math.random()*1080).toFloat()
        camino = (Math.random()*3).toInt()
    }

    fun moverMosca(){
        when(camino){
            0-> {
                x += vel*2
                y += vel

                if(x>=2340&&y>=1080){
                    x = 2340- x
                    y = 1080- y
                }
            }
            1-> {
                x += vel*2
                y -= vel
                if(x>=2340&&y<=0){
                    x = 2340 - x
                    y = 1080 - y
                }
            }
            2-> {
                x -= vel*2
                y += vel
                if(x<=0&&y>=1080){
                    x = 2340 - x
                    y = 1080 - y
                }
            }
            3-> {
                x -= vel*2
                y -= vel
                if(x<=0&&y<=0){
                    x = 2340 - x
                    y = 1080 - y
                }
            }
        }
    }

    fun estaEnArea(toqueX:Float, toqueY:Float):Boolean{

        var x2 = x+mosquita.width
        var y2 = y+mosquita.height

        if(toqueX >= x && toqueX <= x2){
            if(toqueY >= y && toqueY <= y2){
                return true
            }
        }
        return false
    }

    fun pintar(c:Canvas, p:Paint){
        c.drawBitmap(mosquita,x,y,p)
    }
}
