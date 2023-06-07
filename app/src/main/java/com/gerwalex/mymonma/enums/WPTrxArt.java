package com.gerwalex.mymonma.enums;

import androidx.annotation.NonNull;

import com.gerwalex.mymonma.R;


public enum WPTrxArt {
    Kauf {
        @Override
        public long getCatID() {
            return 2001;
        }

        @Override
        public int getKlartextID() {
            return R.string.kauf;
        }
        /*
         * Belegung:
         *
         * Einstand: Summe aller Einstände der zugehörigen Pakete, negativ
         *
         * Haltedauer: (negativ) durchschnittliche Haltedauer der Summe der Einstände. Die
         * durchschnittliche
         * Haltedauer wird errechnet wie folgt:
         *
         * Summe von [(paket1_einstand * paket1_haltedauer), (paket2_einstand *
         * paket2_haltedauer) +...+ (paketN_einstand * paketN_haltedauer)] geteilt durch Summe
         * der Einstände. Durch die Multiplikation werden die einzelnen Pakete gewichtet.
         *
         * amount: menge + kurs, negativ
         *
         *
         */
    }, Verkauf {
        @Override
        public int getKlartextID() {
            return R.string.verkauf;
        }

        @Override
        public long getCatID() {
            return 2050;
        }
    }, WPSplitt {
        @Override
        public int getKlartextID() {
            return R.string.wpsplitt;
        }

        @Override
        public long getCatID() {
            return 2090;
        }
    }, Income {
        @Override
        public int getKlartextID() {
            return R.string.einnahmen;
        }

        @Override
        public boolean isSelecttable() {
            return false;
        }

        @Override
        public long getCatID() {
            return 0;
        }
    }, Stammdaten {
        @Override
        public int getKlartextID() {
            return R.string.stammdaten;
        }

        @Override
        public boolean isSelecttable() {
            return false;
        }

        @Override
        public long getCatID() {
            return 0;
        }
    }, Spin_Off {
        @Override
        public int getKlartextID() {
            return R.string.spinn_off;
        }

        @Override
        public long getCatID() {
            return 2091;
        }
    }, Stueckzins {
        @Override
        public int getKlartextID() {
            return R.string.stueckzins;
        }

        @Override
        public long getCatID() {
            return 2101;
        }
    }, Thesaurierung {
        @Override
        public int getKlartextID() {
            return R.string.thesaurierung;
        }

        @Override
        public long getCatID() {
            return 2103;
        }
    }, SonstEinnahmen {
        @Override
        public int getKlartextID() {
            return R.string.sonstEinnahmen;
        }

        @Override
        public long getCatID() {
            return 2104;
        }
    }, DivIn {
        @Override
        public int getKlartextID() {
            return R.string.dividende;
        }

        @Override
        public long getCatID() {
            return 2105;
        }
    }, DivAus {
        @Override
        public int getKlartextID() {
            return R.string.divAus;
        }

        @Override
        public long getCatID() {
            return 2106;
        }
    }, ZinsEin {
        @Override
        public int getKlartextID() {
            return R.string.zinsEin;
        }

        @Override
        public long getCatID() {
            return 2107;
        }
    }, Ausschuettung {
        @Override
        public int getKlartextID() {
            return R.string.ausschuettung;
        }

        @Override
        public long getCatID() {
            return 2108;
        }
    }, Einnahmen {
        @Override
        public int getKlartextID() {
            return R.string.divEinnahmen;
        }

        @Override
        public long getCatID() {
            return 2111;
        }
    },
    /**
     * Belegung:
     * <p>
     * paketid: _id des verkauften Pakets
     * <p>
     * amount: der realisierte Gewinn eines Paketes, positiv
     * <p>
     * kurs: verkaufkurs (positiv)
     * <p>
     * menge: Menge des aus dem Paekts verkauften Bestands. Negativ
     * <p>
     * einstand: (Anteiliger) Einstandspreis der verkauften Anteile. Negativ
     * <p>
     * haltedauer: Haltedauer des Pakets. Negativ
     */
    Kursgewinn {
        @Override
        public int getKlartextID() {
            return R.string.kursgewinne;
        }

        @Override
        public long getCatID() {
            return 2200;
        }

        @Override
        public boolean isSelecttable() {
            return false;
        }
    },
    /**
     * Belegung:
     * <p>
     * paketid: _id des verkauften Pakets
     * <p>
     * amount: der realisierte Verlust eines Paketes, negativ
     * <p>
     * kurs: verkaufkurs (positiv)
     * <p>
     * menge: Menge des aus dem Paekts verkauften Bestands. Negativ).
     * <p>
     * einstand: (Anteiliger) Einstandspreis der verkauften Anteile. Negativ
     */
    Kursverlust {
        @Override
        public int getKlartextID() {
            return R.string.kursverluste;
        }

        @Override
        public long getCatID() {
            return 2201;
        }

        @Override
        public boolean isSelecttable() {
            return false;
        }
    }, Bankgebuehren {
        @Override
        public int getKlartextID() {
            return R.string.gebuehren;
        }

        @Override
        public long getCatID() {
            return 10015;
        }
    }, Abgeltungssteuer {
        @Override
        public int getKlartextID() {
            return R.string.abgeltSteuer;
        }

        @Override
        public boolean isSelecttable() {
            return false;
        }

        @Override
        public long getCatID() {
            return 2500;
        }
    },
    /**
     * Verrechnung auf Cashkonto. Hier füehrt der Aufruf von getBLZ() zu einer NPE
     */
    Umbuchung {
        @Override
        public int getKlartextID() {
            return R.string.umbuchung;
        }

        @Override
        public boolean isSelecttable() {
            return false;
        }

        @Override
        public long getCatID() {
            throw new NullPointerException("Bei einer Umbuchung ist die catid mit der VerrechnungskontoID zu belegen");
        }
    };
    public static final String WPTRXART = "WPTRXART";

    @NonNull
    public static WPTrxArt find(long catid) {
        for (WPTrxArt art : WPTrxArt.values()) {
            if (art.getCatID() == catid) {
                return art;
            }
        }
        throw new IllegalArgumentException("keine WPTrxArt zu catid gefunden - catid: " + catid);
    }

    public abstract long getCatID();

    /**
     * ResID des jeweils zum dieser Transaktionsrt gehörenden Ertagstextes
     */
    public int getErtragsTextID() {
        return R.string.na;
    }

    /**
     * ResID der Bezeichnung
     */
    public abstract int getKlartextID();

    /**
     * True, wenn diese TrxArt ausgewählt werden kann
     */
    public boolean isSelecttable() {
        return true;
    }
}