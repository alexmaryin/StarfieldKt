import kotlinx.cinterop.CValuesRef
import platform.SDL2.*

fun SDL_RenderFillCircle(renderer: CValuesRef<SDL_Renderer>?, x: Int, y: Int, radius: Int): Int {
    var offsetX = 0
    var offsetY = radius
    var d = radius - 1
    var status = 0

    while (offsetY >= offsetX) {
        status += SDL_RenderDrawLine(renderer, x - offsetY, y + offsetX, x + offsetY, y + offsetX)
        status += SDL_RenderDrawLine(renderer, x - offsetX, y + offsetY, x + offsetX, y + offsetY)
        status += SDL_RenderDrawLine(renderer, x - offsetX, y - offsetY, x + offsetX, y - offsetY)
        status += SDL_RenderDrawLine(renderer, x - offsetY, y - offsetX, x + offsetY, y - offsetX)

        if (status > 0) return -1

        when {
            d >= 2 * offsetX -> {
                d -= 2 * offsetX + 1
                offsetX++
            }
            d < 2 * (radius - offsetY) -> {
                d += 2 * offsetY - 1
                offsetY--
            }
            else -> {
                d += 2 * (offsetY - offsetX - 1)
                offsetY--
                offsetX++
            }
        }
    }
    return status
}