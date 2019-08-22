<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
    <xsl:template match="/">
        <html>
            <head>
                <title>
                    RSS-<xsl:value-of select="rss/channel/title" />
                </title>
                <style type="text/css">
                    body,td {font-size:13px;font-family:Verdana,'宋体';}
                    a { color:#0099ee; text-decoration:none;}
                    a:hover { color:#ff99dd;}
                    .title {font-size:16px; font-weight:bold; border-bottom:solid 1px #0099ee;}
                    .pub_footerall {
                    BORDER-TOP: #ccc 1px solid; text-align:center; line-height:200%; margin-top:20px; padding-top:6px;
                    }
                    .pub_footerall DL {
                    PADDING-BOTTOM: 3px; MARGIN: 0px; PADDING-LEFT: 0px; PADDING-RIGHT: 0px; PADDING-TOP: 3px
                    }
                    .pub_footerall DD A {
                    PADDING-BOTTOM: 0px; PADDING-LEFT: 5px; PADDING-RIGHT: 5px; COLOR: #000; PADDING-TOP: 0px
                    }
                    img{margin-top:20px;width:140px;height:140px;}
                </style>
            </head>
            <body>
                <center>
                    <xsl:apply-templates select="rss/channel" />
                </center>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="channel">

        <table width="90%" cellpadding="0" cellspacing="0" style="margin-bottom:20px;">
            <tr>
                <td align="left">
                    <span style="font-size:24px; font-weight:bold;">
                        <xsl:element name="A">
                            <xsl:attribute name="href">
                                <xsl:value-of select="link"/>
                            </xsl:attribute>
                            <xsl:attribute name="target">
                                _blank
                            </xsl:attribute>
                            <xsl:value-of select="title"/>
                        </xsl:element>
                    </span>
                    <span style="font-size:14px;padding-left:20px;">
                        <xsl:value-of select="description" />
                    </span>
                </td>
                <td align="right">
                    <xsl:element name="A">
                        <xsl:attribute name="href">
                            <xsl:value-of select="image/link"/>
                        </xsl:attribute>
                        <xsl:attribute name="target">_blank</xsl:attribute>
                        <xsl:element name="IMG">
                            <xsl:attribute name="src">
                                <xsl:value-of select="image/url" />
                            </xsl:attribute>
                            <xsl:attribute name="border">0</xsl:attribute>
                        </xsl:element>
                    </xsl:element>
                </td>
            </tr>
        </table>

        <table width="90%" cellpadding="4" cellspacing="0">
            <xsl:for-each select="item">
                <tr>
                    <td class="title">
                        <xsl:element name="A">
                            <xsl:attribute name="href">
                                <xsl:value-of select="link" />
                            </xsl:attribute>
                            <xsl:attribute name="target">_blank</xsl:attribute>
                            <xsl:value-of select="title" />
                        </xsl:element>
                    </td>
                </tr>
                <tr>
                    <td>
                        <xsl:value-of select="description" disable-output-escaping="yes" />
                    </td>
                </tr>
                <tr height="10">
                    <td></td>
                </tr>
            </xsl:for-each>
            <tr>
                <td height="40"></td>
            </tr>
        </table>
    </xsl:template>
</xsl:stylesheet>