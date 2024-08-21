package org.proven.decisions2.Games;


import android.app.Activity;

import org.proven.decisions2.R;

public class Question extends Activity {

    public static int question[] ={
            // Geography
            R.string.geo1,
            R.string.geo2,
            R.string.geo3,
            R.string.geo4,
            R.string.geo5,
            R.string.geo6,
            R.string.geo7,
            R.string.geo8,
            R.string.geo9,
            // Science
            R.string.sci1,
            R.string.sci2,
            R.string.sci3,
            R.string.sci4,
            R.string.sci5,
            R.string.sci6,
            R.string.sci7,
            R.string.sci8,
            R.string.sci9,
            R.string.sci10,
            // Art and Culture
            R.string.art1,
            R.string.art2,
            R.string.art3,
            R.string.art4,
            R.string.art5,
            // Technology
            R.string.tec1,
            R.string.tec2,
            R.string.tec3,
            R.string.tec4,
            R.string.tec5,
            R.string.tec6,
            // Astrology
            R.string.ast1,
            R.string.ast2,
            R.string.ast3,
            R.string.ast4,
            // History
            R.string.htr1,
            R.string.htr2,
            R.string.htr3,
            R.string.htr4,
            R.string.htr5,
            R.string.htr6,
            // Sport
            R.string.spt1,
            R.string.spt2,
            R.string.spt3,
            R.string.spt4,
            R.string.spt5,
            R.string.spt6
    };

    public static int answers[][] = {
            // Geography
            {R.string.south_america, R.string.asia, R.string.north_america, R.string.africa},
            {R.string.tuvalu, R.string.nauru, R.string.vatican, R.string.monaco},
            {R.string.kangchenjunga, R.string.k2, R.string.mounteverest, R.string.lhotse},
            {R.string.peru, R.string.colombia, R.string.argent, R.string.brazil},
            {R.string.cairo, R.string.giza, R.string.alexandria, R.string.luxor},
            {R.string.peso, R.string.euro, R.string.dollar, R.string.real},
            {R.string.melbourne, R.string.sydney, R.string.brisbane, R.string.canberra},
            {R.string.kyoto, R.string.tokyo, R.string.nagoya, R.string.osaka},
            {R.string.amazon, R.string.nile, R.string.missipi, R.string.yangtze},
            // Science
            {R.string.stephen_hawking, R.string.galileo_galilei, R.string.isaac_newton, R.string.albert_einstein},
            {R.string.brain, R.string.heart, R.string.skin, R.string.liver},
            {R.string.au, R.string.cu, R.string.fe, R.string.ag},
            {R.string.louis_pasteur, R.string.marie_curie, R.string.charles_darwin, R.string.alexander_fleming},
            {R.string.photosynthesis, R.string.respiration, R.string.fermentation, R.string.krebs_cycle},
            {R.string.meiosis, R.string.mitosis, R.string.respiration, R.string.fermentation},
            {R.string.cheetah, R.string.peregrine_falcon, R.string.sailfish, R.string.black_marlin},
            {R.string.e208, R.string.e212, R.string.e206, R.string.e210},
            {R.string.n3, R.string.n4, R.string.n5, R.string.n6},
            {R.string.atlantic, R.string.pacific, R.string.indian, R.string.southern},
            // Art
            {R.string.claude_monet, R.string.leonardo_da_vinci, R.string.pablo_picasso, R.string.vincent_van_gogh},
            {R.string.pablo_picasso, R.string.leonardo_da_vinci, R.string.vincent_van_gogh, R.string.salvador_dali},
            {R.string.ernest_hemingway, R.string.harper_lee, R.string.mark_twain, R.string.jd_salinger},
            {R.string.william_shakespeare, R.string.oscar_wilde, R.string.george_bernard_shaw, R.string.samuel_beckett},
            {R.string.george_lucas, R.string.martin_scorsese, R.string.steven_spielberg, R.string.francis_ford_coppola},
            // Technology
            {R.string.nokia, R.string.samsung, R.string.google, R.string.apple},
            {R.string.samsung, R.string.google, R.string.apple, R.string.nokia},
            {R.string.instagram, R.string.whatsapp, R.string.facebook, R.string.youtube},
            {R.string.java, R.string.python, R.string.kotlin, R.string.notepad},
            {R.string.copper, R.string.silver, R.string.gold, R.string.aluminum},
            {R.string.electricity, R.string.diesel, R.string.gasoline, R.string.natural_gas},
            // Astrology
            {R.string.mars, R.string.saturn, R.string.jupiter, R.string.uranus},
            {R.string.neptune, R.string.mercury, R.string.venus, R.string.mars},
            {R.string.outer_planet, R.string.supernova, R.string.quasar, R.string.ion_thruster},
            {R.string.nebula, R.string.comet, R.string.galaxy, R.string.asteroid},
            // History
            {R.string.augustus, R.string.julius_caesar, R.string.nero, R.string.caligula},
            {R.string.year_1786, R.string.year_1787, R.string.year_1788, R.string.year_1789},
            {R.string.usa, R.string.china, R.string.ussr, R.string.japan},
            {R.string.pearl_harbor_attack, R.string.battle_of_stalingrad, R.string.d_day_invasion, R.string.atomic_bombing_of_hiroshima},
            {R.string.mikhail_gorbachev, R.string.vladimir_lenin, R.string.joseph_stalin, R.string.leon_trotsky},
            {R.string.television, R.string.lightbulb, R.string.automobile, R.string.telephone},
            //Sport
            {R.string.five, R.string.eleven, R.string.eight, R.string.six},
            {R.string.eight_feet, R.string.ten_feet, R.string.nine_feet, R.string.seven_feet},
            {R.string.french_open, R.string.us_open, R.string.australian_open, R.string.wimbledon},
            {R.string.two_points, R.string.four_points, R.string.six_points, R.string.three_points},
            {R.string.vuelta_a_espana, R.string.tour_de_suisse, R.string.giro_d_italia, R.string.tour_de_france},
            {R.string.four_outs, R.string.three_outs, R.string.five_outs, R.string.two_outs}
    };

    public static int correctAnswer[] = {
            // Geography
            1, 2, 2, 3, 0, 3, 3, 1, 1,
            // Science
            3, 2, 0, 1, 0, 1, 0, 2, 0, 1,
            // Art and Culture
            1, 2, 1, 0, 2,
            // Technology
            2, 2, 3, 3, 1, 0,
            // Astrology
            2, 1, 1, 0,
            //History
            0, 3, 2, 0, 1, 3,
            //Sport
            1, 1, 2, 2, 3, 0
    };

    public static int category[] = {
            R.string.geography, R.string.geography, R.string.geography, R.string.geography, R.string.geography, R.string.geography, R.string.geography, R.string.geography, R.string.geography,
            R.string.science, R.string.science, R.string.science, R.string.science, R.string.science, R.string.science, R.string.science,R.string.science,R.string.science,R.string.science,
            R.string.art, R.string.art, R.string.art, R.string.art, R.string.art,
            R.string.tecno, R.string.tecno, R.string.tecno, R.string.tecno, R.string.tecno, R.string.tecno,
            R.string.astro, R.string.astro,R.string.astro, R.string.astro,
            R.string.histo, R.string.histo,R.string.histo,R.string.histo,R.string.histo,R.string.histo,
            R.string.sport, R.string.sport,R.string.sport,R.string.sport,R.string.sport,R.string.sport,
    };

}



