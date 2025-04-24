package com.yesnoheun3.makeyourmorning.common.data

import java.time.LocalDateTime

data class BlockTime (
    val id: String = LocalDateTime.now().toString(),
    val minute: Int,
    val type: BlockType
)