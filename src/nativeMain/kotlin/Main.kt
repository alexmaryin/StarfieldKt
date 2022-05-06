import platform.SDL2.SDL_QUIT
import platform.SDL2.SDL_SetRenderDrawColor

fun main() {

    SDLEngine(Consts.WIDTH, Consts.HEIGHT) {

        val fpsCounter = FPSCounter(window)
        val stars = List(Consts.STARS_COUNT) { Star() }

        addEventListener("starfield") { event ->
            if (event.type == SDL_QUIT) stopLoop()
        }

        startInfiniteLoop {
            onEachFrame {
                fpsCounter.checkFPS()
                stars.forEach { star ->
                    star.process()
                    val color = star.brightness.toUInt().toUByte()
                    SDL_SetRenderDrawColor(this, color, color, color, 255)
                    val x = star.viewX + Consts.WIDTH / 2
                    val y = star.viewY + Consts.HEIGHT / 2
                    SDL_RenderFillCircle(this, x, y, star.radius.toInt())
                }
            }
        }
    }
}