<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="html" encoding="UTF-8" indent="yes"/>

    <xsl:template match="/channel">
        <html>
            <head>
                <title>Waluty - <xsl:value-of select="baseCurrency"/></title>
                <style>
                    body { font-family: Arial, sans-serif; background-color: #f9f9f9; color: #333; }
                    h1 { color: #0066cc; }
                    table { width: 80%; border-collapse: collapse; margin: 20px 0; }
                    th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }
                    th { background-color: #0066cc; color: white; }
                    tr:nth-child(even) { background-color: #f2f2f2; }
                </style>
            </head>
            <body>
                <h1>Kursy walut względem <xsl:value-of select="baseCurrency"/></h1>
                <p>Liczba walut: <xsl:value-of select="count(item)"/></p>
                <table>
                    <tr>
                        <th>Waluta</th>
                        <th>Nazwa</th>
                        <th>Kurs</th>
                        <th>Odwrócony kurs</th>
                    </tr>
                    <xsl:for-each select="item">
                        <tr>
                            <td><xsl:value-of select="targetCurrency"/></td>
                            <td><xsl:value-of select="targetName"/></td>
                            <td><xsl:value-of select="exchangeRate"/></td>
                            <td><xsl:value-of select="inverseRate"/></td>
                        </tr>
                    </xsl:for-each>
                </table>
                <p>Dane opublikowano: <xsl:value-of select="pubDate"/></p>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>
