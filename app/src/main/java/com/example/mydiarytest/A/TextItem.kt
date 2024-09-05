package com.example.mydiarytest.A

data class TextItem(
    val textId : Int,
    val textIndex : Int,
    val content : String,
) : NoteContentItem(textId, textIndex)

data class TagsItem(
    val tagId : Int,
    val tagIndex : Int,
    val contents : List<String>,
) : NoteContentItem(tagId, tagIndex)


data class MarkListItem(
    val idMarkList: Int,
    val indexMarkList: Int,
    val type : Int,
    val contents : List<String>,
) : NoteContentItem(idMarkList, indexMarkList)

data class PromptItem(
    val idPrompt: Int,
    val indexPrompt: Int,
    val question: String,
): NoteContentItem(idPrompt, indexPrompt)

data class ImageItem(
    val idImage: Int,
    val indexImage: Int,
    val src: String,
    val width: Int,
    val height: Int,
) : NoteContentItem(idImage,indexImage)

data class Note (
    val id : Int,
    val timeCreate : Long,
    val timeEdit : Long,
    val title : String,
    val question : String,
    val moodId : String,
    val backgroundId : Int,
    val listNoteItems : List<NoteContentItem>,
    val listSticker : List<NoteStickerItem>
)

class NoteStickerItem(
    val id : String,
    val url : String,
    val x : Int,
    val y : Int,
    val width : Int,
    val height: Int,
    val rotate : Int,
    val isFlip : Boolean,
)

val listNoteItems = mutableListOf<NoteContentItem>().apply {
    add(PromptItem(0,0,"question 1 ?"))
    add(MarkListItem(1,1,15, listOf("l1", "l2","l3")))
    add(MarkListItem(2,2,15, listOf("l1", "l2","l3")))
    add(TagsItem(3,this.size-1, listOf("tag1", "tag2","tag3")))
    add(TagsItem(4,this.size-1, listOf("tag1", "tag2","tag3")))
    add(TextItem(5,3, "text here"))
    add(TextItem(6,4, "text here"))
    add(TextItem(7,5, "text here"))
    add(ImageItem(8,6, "src Image 1", 300,300))
    add(ImageItem(9,7, "src Image 2", 600,500))
}