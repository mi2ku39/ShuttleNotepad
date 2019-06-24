package jp.ghostserver.ghostshuttle.models.memo

class Memo(
        var id: Int,
        var title: String,
        var text: String
) {
    companion object {
        val memos: List<Memo>
            get() {
                TODO()
            }
    }
}