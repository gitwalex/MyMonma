Cat: (nur accounts)
select _id as id, name, description, obercatid, supercatid, catclassid,
 incomecat, ausgeblendet from Type_Cat where catclassid = 2 and _id < 1000

Account:
select _id as id, _id as catid, inhaber,currency, iban, blz, bezeichnung,
0 as creditlimit, verrechnungskonto, openDate
,case kontotyp
      when '0'  then "Giro"
      when '1' then  "Kreditkarte"
      when '2' then "Depot"
      when 3  then "Vermoegen"
      when 4 then  "Anlagen"
      when 5 then  "Verbindlichkeiten"
      when 6 then "Immobilien"

	  end as  kontotyp
from account

WPStamm:
select _id as id, _id as partnerid, wpname as name, wpkenn, isin, wptyp, risiko, isBeobachten as beobachten,
ertragestimated as estEarning
from WPStammdaten where _id > 0

Partner:
select _id as id, name from Partnerstammdaten where _id > 1


WPKurs:
select _id as id, btag, wpid, kurs from WPKurs

Cashtrx:
- LF weg :
UPDATE CashTrans SET memo=REPLACE(memo,Char(10), ' ')
select _id as id, btag, accountid, catid ,partnerid, amount , memo, transferid, 0 as isUmbuchung from CashTrans

TrxRegelm:
- LF weg :
UPDATE TrxRegelm SET memo=REPLACE(memo,Char(10), ' ')
select _id as id, btag, accountid, catid, partnerid, memo, amount, transferid, count as cnt, intervallid, last , isUltimo, 0 as isUmbuchung from TrxRegelm

WPTrx:
select _id as id, accountid, btag, catid, einstand, ertrag, cast (haltedauer as int) as haltedauer, kurs, menge, paketid, wpid, zinszahl from WPTrx