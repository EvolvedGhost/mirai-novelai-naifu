package translate.data

import java.util.*


data class EHTAG(
    val repo: String,
    val head: EHTAGHead,
    val version: Int = 0,
    val data: List<EHTAGData>,
)

data class EHTAGHead(
    val sha: String,
    val message: String,
    val author: EHTAGHeadInfo,
    val committer: EHTAGHeadInfo,
)

data class EHTAGHeadInfo(
    val name: String,
    val email: String,
    val `when`: Date,
)

data class EHTAGData(
    val data: Map<String, EHTAGDataData>,
)

data class EHTAGDataData(
    var name: String,
    var intro: String,
    var links: String,
)