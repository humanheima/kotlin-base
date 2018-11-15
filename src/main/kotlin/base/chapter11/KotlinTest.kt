package base.chapter11

import kotlinx.html.stream.createHTML
import kotlinx.html.table
import kotlinx.html.td
import kotlinx.html.tr

/**
 * Created by dmw on 2018/11/15.
 * Desc:
 */

fun createSimpleTable() = createHTML().table {
    tr {
        td { +"cell" }
    }
}

fun main(args: Array<String>) {
    print(createSimpleTable())

}