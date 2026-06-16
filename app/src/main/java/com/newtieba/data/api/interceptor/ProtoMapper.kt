package com.newtieba.data.api.interceptor

import com.newtieba.data.model.ContentPiece
import com.newtieba.data.model.ContentType
import com.newtieba.data.model.Forum
import com.newtieba.data.model.Post
import com.newtieba.data.model.Thread
import com.newtieba.data.model.TiebaUser

/**
 * Proto2 → Domain Model 映射器
 *
 * 将 ProtoReader 读取的二进制数据映射为业务模型。
 * 字段号对应 tbclient/*.proto 中的定义。
 */
object ProtoMapper {

    // ── FrsPageResIdl 解析 ────────────────────────────────────

    /**
     * FrsPageResIdl → data → thread_list → List<Thread>
     *
     * proto 结构:
     *   FrsPageResIdl
     *     data (field 1)
     *       thread_list (field 2, repeated)
     *         id (field 1, uint64 = tid)
     *         title (field 3, string)
     *         reply_num (field 4, uint64)
     *         author (field 7, message)
     *         last_time_int (field 11, uint64)
     *         abstract (field 21, string)
     *         media (field 22, repeated)
     *         agree_num (field 15, uint32)
     */
    fun parseThreads(responseBytes: ByteArray): List<Thread> {
        val rootReader = ProtoReader(responseBytes)

        // 从 root 中提取 data wrapper message (field 1)
        val dataWrapper = rootReader.getMessage(1) ?: return emptyList()
        val dataReader = ProtoReader(dataWrapper)

        // 从 data 中提取 repeated thread_list (field 2)
        val threadMessages = dataReader.getRepeatedMessage(2)

        return threadMessages.mapNotNull { threadBytes ->
            parseThread(threadBytes)
        }
    }

    private fun parseThread(bytes: ByteArray): Thread? {
        val reader = ProtoReader(bytes)
        val tid = reader.getUInt64(1)
        if (tid == 0L) return null

        return Thread(
            tid = tid,
            title = reader.getString(3),
            replyNum = reader.getUInt32(4),
            isGood = reader.getUInt32(5) == 1,
            isTop = reader.getUInt32(6) == 1,
            author = parseAuthor(reader.getMessage(7)),
            createTime = reader.getUInt64(8),
            lastTime = reader.getUInt64(11),
            agreeNum = reader.getUInt32(15),
            abstract = reader.getString(21),
            images = parseMediaImages(reader.getRepeatedMessage(22)),
        )
    }

    private fun parseAuthor(bytes: ByteArray?): TiebaUser {
        if (bytes == null) return TiebaUser(0, "", "")
        val reader = ProtoReader(bytes)
        return TiebaUser(
            uid = reader.getUInt64(1),
            name = reader.getString(2),
            portrait = reader.getString(3),
            level = reader.getUInt32(12),
            intro = reader.getString(10),
            sex = reader.getUInt32(5),
        )
    }

    private fun parseMediaImages(mediaMessages: List<ByteArray>): List<String> {
        return mediaMessages.mapNotNull { mediaBytes ->
            val reader = ProtoReader(mediaBytes)
            // src = field 1, original_src = field 2
            val src = reader.getString(1)
            src.ifEmpty { null }
        }
    }

    // ── PbPageResIdl 解析 ─────────────────────────────────────

    /**
     * PbPageResIdl → data → post_list → List<Post>
     *
     * proto 结构:
     *   PbPageResIdl
     *     data (field 1)
     *       thread (field 1, ThreadInfo)
     *       post_list (field 2, repeated)
     *         id (field 1, uint64 = pid)
     *         floor (field 2, uint32)
     *         author (field 3, message)
     *         content (field 15, repeated PbContent)
     *         agree_num (field 10, uint32)
     *         sub_post_number (field 8, uint32)
     *         time (field 6, uint64)
     */
    fun parsePosts(responseBytes: ByteArray): List<Post> {
        val rootReader = ProtoReader(responseBytes)
        val dataWrapper = rootReader.getMessage(1) ?: return emptyList()
        val dataReader = ProtoReader(dataWrapper)

        val postMessages = dataReader.getRepeatedMessage(2)
        return postMessages.mapNotNull { postBytes ->
            parsePost(postBytes)
        }
    }

    private fun parsePost(bytes: ByteArray): Post? {
        val reader = ProtoReader(bytes)
        val pid = reader.getUInt64(1)
        if (pid == 0L) return null

        return Post(
            pid = pid,
            floor = reader.getUInt32(2),
            author = parseAuthor(reader.getMessage(3)),
            time = reader.getUInt64(6),
            subPostCount = reader.getUInt32(8),
            agreeNum = reader.getUInt32(10),
            content = parseContentList(reader.getRepeatedMessage(15)),
            images = parseContentImages(reader.getRepeatedMessage(15)),
        )
    }

    /**
     * 解析 PbContent repeated list → List<ContentPiece>
     *
     * PbContent:
     *   type (field 1, int32): 0=文字, 1=链接, 2=表情, 3=图片, 4=@用户, 9=视频
     *   text (field 2, string)
     *   link (field 5, string)
     *   src  (field 6, string)
     */
    private fun parseContentList(contentMessages: List<ByteArray>): List<ContentPiece> {
        return contentMessages.mapNotNull { contentBytes ->
            val reader = ProtoReader(contentBytes)
            val type = reader.getUInt32(1)
            val text = reader.getString(2)
            val link = reader.getString(5)
            val src = reader.getString(6)

            // 纯图片/视频 content piece 只索引图片，不生成 ContentPiece
            if (type == 3 || type == 9) return@mapNotNull null

            ContentPiece(
                type = when (type) {
                    0 -> ContentType.TEXT
                    1 -> ContentType.LINK
                    2 -> ContentType.EMOJI
                    4 -> ContentType.AT_USER
                    9 -> ContentType.VIDEO
                    else -> ContentType.TEXT
                },
                text = text,
                link = link,
                src = src,
            )
        }
    }

    /**
     * 从 PbContent repeated list 中提取图片 URL
     */
    private fun parseContentImages(contentMessages: List<ByteArray>): List<String> {
        return contentMessages.mapNotNull { contentBytes ->
            val reader = ProtoReader(contentBytes)
            val type = reader.getUInt32(1)
            if (type == 3 || type == 9) {
                val src = reader.getString(6)
                src.ifEmpty { null }
            } else null
        }
    }

    // ── 吧信息解析 ─────────────────────────────────────────────

    fun parseForumInfo(responseBytes: ByteArray): Forum? {
        val rootReader = ProtoReader(responseBytes)
        val dataWrapper = rootReader.getMessage(1) ?: return null
        val dataReader = ProtoReader(dataWrapper)
        val forumMsg = dataReader.getMessage(1) ?: return null

        val reader = ProtoReader(forumMsg)
        return Forum(
            fid = reader.getUInt64(1),
            name = reader.getString(2),
            memberNum = reader.getUInt32(3),
            threadNum = reader.getUInt32(4),
            avatar = reader.getString(6),
            isFollowed = reader.getUInt32(10) == 1,
        )
    }
}
