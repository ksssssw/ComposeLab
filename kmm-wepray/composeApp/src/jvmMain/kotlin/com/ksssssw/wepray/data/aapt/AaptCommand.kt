package com.ksssssw.wepray.data.aapt

/**
 * AAPT(Android Asset Packaging Tool) 명령어를 나타내는 sealed class
 * APK 파일에서 정보를 추출하는 데 사용됩니다.
 */
sealed class AaptCommand {
    abstract fun toCommandArgs(): List<String>

    /**
     * APK의 상세 정보를 추출 (dump badging)
     *
     * @property apkPath APK 파일 경로
     */
    data class DumpBadging(
        val apkPath: String,
    ) : AaptCommand() {
        override fun toCommandArgs(): List<String> = listOf("dump", "badging", apkPath)
    }

    /**
     * APK의 권한 정보를 추출 (dump permissions)
     *
     * @property apkPath APK 파일 경로
     */
    data class DumpPermissions(
        val apkPath: String,
    ) : AaptCommand() {
        override fun toCommandArgs(): List<String> = listOf("dump", "permissions", apkPath)
    }

    /**
     * APK의 XML 리소스를 추출 (dump xmltree)
     *
     * @property apkPath APK 파일 경로
     * @property assetPath APK 내부 리소스 경로 (예: AndroidManifest.xml)
     */
    data class DumpXmlTree(
        val apkPath: String,
        val assetPath: String = "AndroidManifest.xml",
    ) : AaptCommand() {
        override fun toCommandArgs(): List<String> = listOf("dump", "xmltree", apkPath, assetPath)
    }
}