<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="html" encoding="UTF-8" indent="yes"/>

    <xsl:template match="/channel">
        <html>
            <head>
                <title>Kursy walut - <xsl:value-of select="baseCurrency"/></title>
                <style>
                    table { border-collapse: collapse; width: 60%; }
                    th, td { border: 1px solid #888; padding: 5px; text-align: left; }
                    th { background-color: #eee; }
                </style>
            </head>
            <body>
                <h1>Kursy walut względem <xsl:value-of select="baseCurrency"/></h1>
                <table>
                    <tr>
                        <th>Waluta docelowa</th>
                        <th>Nazwa</th>
                        <th>Kurs</th>
                    </tr>
                    <xsl:for-each select="item">
                        <tr>
                            <td><xsl:value-of select="targetCurrency"/></td>
                            <td><xsl:value-of select="targetName"/></td>
                            <td><xsl:value-of select="exchangeRate"/></td>
                        </tr>
                    </xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>
