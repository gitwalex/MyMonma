package com.gerwalex.mymonma.enums;

import androidx.annotation.StringRes;

import com.gerwalex.mymonma.R;

/**
 * Liste der Wertpapiertypen
 */
public enum WPTyp {
    Aktie(R.string.aktie) {
        @Override
        public WPTrxArt getEinnahmeart() {
            return WPTrxArt.DivIn;
        }

        @Override
        public int getEinnahmeartTextResID() {
            return R.string.dividende;
        }

        @Override
        public boolean areFractionAllowed() {
            return false;
        }

        @Override
        public int getRisiko() {
            return 2;
        }
    }//
    , Fonds(R.string.fonds) {
        @Override
        public WPTrxArt getEinnahmeart() {
            return WPTrxArt.Ausschuettung;
        }

        @Override
        public int getEinnahmeartTextResID() {
            return R.string.ausschuettung;
        }

        @Override
        public int getRisiko() {
            return 3;
        }
    }//
    , Anleihe(R.string.anleihe) {
        @Override
        public WPTrxArt getEinnahmeart() {
            return WPTrxArt.ZinsEin;
        }

        @Override
        public boolean areFractionAllowed() {
            return false;
        }

        @Override
        public int getEinnahmeartTextResID() {
            return R.string.zinsEin;
        }

        @Override
        public int getRisiko() {
            return 4;
        }
    }//
    , Zertifikat(R.string.zertifikat) {
        @Override
        public WPTrxArt getEinnahmeart() {
            return null;
        }

        @Override
        public boolean areFractionAllowed() {
            return false;
        }

        @Override
        public int getEinnahmeartTextResID() {
            return R.string.einnahmen;
        }

        @Override
        public int getRisiko() {
            return 2;
        }
    }//
    , ETF(R.string.etf) {
        @Override
        public WPTrxArt getEinnahmeart() {
            return WPTrxArt.Ausschuettung;
        }

        @Override
        public int getEinnahmeartTextResID() {
            return R.string.ausschuettung;
        }

        @Override
        public int getRisiko() {
            return 2;
        }
    }//
    , Optionschein(R.string.optionen) {
        @Override
        public WPTrxArt getEinnahmeart() {
            return null;
        }

        @Override
        public boolean areFractionAllowed() {
            return false;
        }

        @Override
        public int getEinnahmeartTextResID() {
            return R.string.na;
        }

        @Override
        public int getRisiko() {
            return 1;
        }
    }//
    , Index(R.string.index) {
        @Override
        public WPTrxArt getEinnahmeart() {
            return null;
        }

        @Override
        public int getEinnahmeartTextResID() {
            return R.string.na;
        }

        @Override
        public int getRisiko() {
            return 3;
        }
    },//
    Discount(R.string.discount) {
        @Override
        public WPTrxArt getEinnahmeart() {
            return null;
        }

        @Override
        public boolean areFractionAllowed() {
            return false;
        }

        @Override
        public int getEinnahmeartTextResID() {
            return R.string.na;
        }

        @Override
        public int getRisiko() {
            return 1;
        }
    },//
    ;
    @StringRes
    public final int klartextID;

    WPTyp(int klartextID) {
        this.klartextID = klartextID;
    }

    /**
     * Sind beim Kauf oder Verkauf Bruchteile erlaubt?
     *
     * @return erlaubt => true (default).
     */
    public boolean areFractionAllowed() {
        return true;
    }

    public abstract WPTrxArt getEinnahmeart();

    @StringRes
    public abstract int getEinnahmeartTextResID();

    public abstract int getRisiko();
}


