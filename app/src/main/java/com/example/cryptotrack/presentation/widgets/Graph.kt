package com.example.cryptotrack.presentation.widgets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cryptotrack.domain.model.CoinChart
import com.example.cryptotrack.domain.model.CoinsChartList


@Composable
@Preview(showBackground = true)
fun Graph() {
    val points = CoinsChartList(
        list = listOf(
            CoinChart(1777838406341, 78772.9363875891),
            CoinChart(1777842024969, 78882.4135264458),
            CoinChart(1777845602579, 78784.5653823138),
            CoinChart(1777849218129, 79164.9126266481),
            CoinChart(1777852824199, 78542.8080371482),
            CoinChart(1777856421733, 78523.0920951143),
            CoinChart(1777860011798, 79525.0486011737),
            CoinChart(1777863647107, 80247.5743391045),
            CoinChart(1777867219152, 80254.0760312916),
            CoinChart(1777870804028, 80296.7236276055),
            CoinChart(1777874449287, 80010.7868917525),
            CoinChart(1777878026455, 79920.4474961266),
            CoinChart(1777881653151, 79689.3122143952),
            CoinChart(1777885252207, 79714.0128258611),
            CoinChart(1777888851475, 79832.3378237271),
            CoinChart(1777892430233, 78911.1707249776),
            CoinChart(1777896029630, 78794.5386775698),
            CoinChart(1777899646426, 78946.7438279322),
            CoinChart(1777903219620, 78795.3827203887),
            CoinChart(1777906826294, 80262.8144126693),
            CoinChart(1777910459053, 79961.4796572423),
            CoinChart(1777914029774, 79997.7587206583),
            CoinChart(1777917624474, 80468.0775179202),
            CoinChart(1777921225417, 80114.391693814),
            CoinChart(1777924836109, 80052.0708073038),
            CoinChart(1777928449527, 79929.4560889152),
            CoinChart(1777932046495, 80248.5577046559),
            CoinChart(1777935654225, 80060.1070554768),
            CoinChart(1777939220337, 79824.4033120475),
            CoinChart(1777942842625, 80138.6811920848),
            CoinChart(1777946436397, 80325.9897882169),
            CoinChart(1777950042948, 80502.6608897079),
            CoinChart(1777953641908, 80862.3298628971),
            CoinChart(1777957216870, 80879.6416976796),
            CoinChart(1777960831571, 80964.3289291119),
            CoinChart(1777964458056, 80929.426912272),
            CoinChart(1777968059885, 80832.0152095033),
            CoinChart(1777971629966, 80777.5328985154),
            CoinChart(1777975269759, 80559.8777398346),
            CoinChart(1777978810508, 80807.3265632356),
            CoinChart(1777982450660, 80980.8874029157),
            CoinChart(1777986059256, 81269.26250467),
            CoinChart(1777989674421, 81344.9530195381),
            CoinChart(1777993211258, 81320.6271467758),
            CoinChart(1777996849284, 81531.4416821765),
            CoinChart(1778000429851, 81239.0870142723),
            CoinChart(1778004027681, 81263.6923534597),
            CoinChart(1778007641471, 81505.5389396501),
            CoinChart(1778011242936, 81610.0981394073),
            CoinChart(1778014849608, 81640.7660677483),
            CoinChart(1778018415009, 81435.04217473),
            CoinChart(1778022048759, 81102.490555625),
            CoinChart(1778025642626, 80925.0936549508),
            CoinChart(1778029246577, 81025.086565407),
            CoinChart(1778032811267, 81397.411569984),
            CoinChart(1778036422686, 81358.3854897571),
            CoinChart(1778040016151, 81584.741663066),
            CoinChart(1778043630236, 81306.1001255984),
            CoinChart(1778047253578, 81297.7233072276),
            CoinChart(1778050834724, 81473.5665929374),
            CoinChart(1778054436137, 81329.1136388315),
            CoinChart(1778058036661, 81899.3024396849),
            CoinChart(1778061612605, 81952.1101049097),
            CoinChart(1778065257616, 82290.684075385),
            CoinChart(1778068855127, 82496.2082074518),
            CoinChart(1778072407183, 82205.6020019561),
            CoinChart(1778076044107, 81706.995476948),
            CoinChart(1778079641537, 81514.5652221345),
            CoinChart(1778083250319, 81680.5268978182),
            CoinChart(1778086844518, 81633.8457400525),
            CoinChart(1778090478590, 81442.953331924),
            CoinChart(1778094050023, 81360.0380176658),
            CoinChart(1778097640265, 81467.3822808094),
            CoinChart(1778101235035, 81396.9085934587),
            CoinChart(1778104833775, 81535.5605118717),
            CoinChart(1778108430621, 81341.728242803),
            CoinChart(1778112043677, 81424.9951565033),
            CoinChart(1778115651201, 81025.8698434047),
            CoinChart(1778119221779, 81164.7659265684),
            CoinChart(1778122847617, 81101.1600317355),
            CoinChart(1778126426688, 80866.4275354731),
            CoinChart(1778130041167, 80953.3833159498),
            CoinChart(1778133629501, 81039.7898976174),
            CoinChart(1778137246447, 81397.1307077436),
            CoinChart(1778140827111, 81497.7290686523),
            CoinChart(1778144450018, 81212.9764377061),
            CoinChart(1778148066904, 80923.641056326),
            CoinChart(1778151629356, 80829.4884578169),
            CoinChart(1778155206855, 80858.046436636),
            CoinChart(1778158855622, 81158.0261862118),
            CoinChart(1778162410793, 80518.0855060595),
            CoinChart(1778166062945, 80160.4622840528),
            CoinChart(1778169627776, 79895.4846846241),
            CoinChart(1778173271652, 79785.5849215548),
            CoinChart(1778176836783, 80080.2410375561),
            CoinChart(1778180458078, 80208.2025998162),
            CoinChart(1778184004248, 80086.8298766476),
            CoinChart(1778187652509, 79864.2433493356),
            CoinChart(1778191251762, 79778.9710136237),
            CoinChart(1778194855585, 79903.1331572948),
            CoinChart(1778198405244, 80022.0412500642),
            CoinChart(1778202073615, 79773.7728148881),
            CoinChart(1778205637934, 79726.5063910817),
            CoinChart(1778209228351, 79467.2776985759),
            CoinChart(1778212814084, 79565.8841527136),
            CoinChart(1778216438884, 79643.4351093555),
            CoinChart(1778220035144, 79621.6688325127),
            CoinChart(1778223635816, 79482.7628778405),
            CoinChart(1778227207120, 79666.4419881599),
            CoinChart(1778230843846, 79901.0194974742),
            CoinChart(1778234435344, 79852.0837424096),
            CoinChart(1778238040409, 80195.2962800901),
            CoinChart(1778241650450, 80218.6969587907),
            CoinChart(1778245257951, 80047.6587379187),
            CoinChart(1778248852100, 79588.2388569291),
            CoinChart(1778252428461, 80097.3082184175),
            CoinChart(1778256055356, 80104.6604432822),
            CoinChart(1778259643496, 79799.2474834811),
            CoinChart(1778263253315, 80026.2054111085),
            CoinChart(1778266824329, 80134.6910565651),
            CoinChart(1778270438222, 80095.5149312719),
            CoinChart(1778274035999, 80115.9908046185),
            CoinChart(1778277639322, 80222.9893721745),
            CoinChart(1778281241591, 80272.1075515676),
            CoinChart(1778284830511, 80189.0650356785),
            CoinChart(1778288472532, 80227.0511784074),
            CoinChart(1778292056529, 80338.7426209578),
            CoinChart(1778295606027, 80403.2633678891),
            CoinChart(1778299229958, 80381.2977396444),
            CoinChart(1778302860807, 80374.1969801594),
            CoinChart(1778306455441, 80384.3857453806),
            CoinChart(1778310034180, 80225.0855390632),
            CoinChart(1778313620685, 80217.3820687777),
            CoinChart(1778317210776, 80393.6376791175),
            CoinChart(1778320817112, 80205.7613054906),
            CoinChart(1778324464837, 80252.188341319),
            CoinChart(1778328023893, 80350.3611638508),
            CoinChart(1778331629436, 80385.0903436548),
            CoinChart(1778335255965, 80375.0281286806),
            CoinChart(1778338828716, 80321.0822600879),
            CoinChart(1778342439151, 80513.2583951042),
            CoinChart(1778346011378, 80630.9162025437),
            CoinChart(1778349673657, 80785.8625816005),
            CoinChart(1778353241248, 80879.9617492935),
            CoinChart(1778356848087, 80894.7301286951),
            CoinChart(1778360412832, 80788.8733783315),
            CoinChart(1778364046614, 80783.0668823158),
            CoinChart(1778367654295, 80742.8167737424),
            CoinChart(1778371221244, 80678.0339429646),
            CoinChart(1778374849367, 80636.1743856982),
            CoinChart(1778378444524, 80646.3677133728),
            CoinChart(1778382042298, 80740.6822406661),
            CoinChart(1778385639559, 80784.8604494136),
            CoinChart(1778389224394, 80756.8406764015),
            CoinChart(1778392853710, 80687.8176334027),
            CoinChart(1778396449530, 80707.175503932),
            CoinChart(1778400048424, 80713.3827875701),
            CoinChart(1778403612115, 80807.2696770732),
            CoinChart(1778407252849, 80789.3917124072),
            CoinChart(1778410836713, 80866.4955562936),
            CoinChart(1778414454370, 80823.2776545626),
            CoinChart(1778418045367, 80887.2745682041),
            CoinChart(1778421633610, 80934.7288755923),
            CoinChart(1778425218573, 80932.1665169071),
            CoinChart(1778428846028, 81405.1034800138),
            CoinChart(1778432448799, 81323.9841890025),
            CoinChart(1778436033629, 81443.3651760244),
            CoinChart(1778439645457, 81264.6051640281),
            CoinChart(1778442862000, 81322.89971914)
        )
    )

    val chartData = points.list


    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
            .background(Color.White)
    ) {
        val topPadding = size.height * 0.1f
        val bottomPadding = size.height * 0.02f


        val graphHeight = size.height - topPadding - bottomPadding

        val maxPrice = chartData.maxOf{it.price}
        val minPrice = chartData.minOf{it.price}

        val priceRange = (maxPrice - minPrice).takeIf { it != 0.0 } ?: 1.0

        val maxTime = chartData.maxOf{it.time}
        val minTime = chartData.minOf{it.time}

        val timeRange = (maxTime - minTime).takeIf { it != 0L } ?: 1L


        val path = Path()
        chartData.forEachIndexed { index, value ->
            val x = ((value.time - minTime) / timeRange.toFloat() * size.width)
            val normalizedY = ((value.price - minPrice) / priceRange).toFloat()
            val y = topPadding + graphHeight * (1f - normalizedY)

            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }

        }
        drawPath(
            path = path,
            color = Color.Green,
            style = Stroke(
                width = 5f
            ),
        )
    }
}