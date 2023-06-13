Cat: (nur accounts)
select _id as id, name, description, obercatid, supercatid, catclassid,
 incomecat, ausgeblendet, cnt from Type_Cat where catclassid = 2

Account:
select _id as id, _id as cat, name,inhaber,currency, iban, blz, bezeichnung,
0 as creditlimit, verrechnungskonto, kontotyp, openDate
from Account

WPStamm:
select _id as id, _id as partnerid, wpname as name, wpkenn, isin, wptyp, risiko, isBeobachten as beobachten,
ertragestimated as estEarning
from WPStammdaten where _id > 0

Partner:
select _id as id, name
from Partner where _id > 1


WPKurs:
select _id as id, btag, wpid, kurs
from WPKurs

Cashtrx:
- LF weg :
UPDATE CashTrans SET memo=REPLACE(memo,Char(10), ' ')
select _id as id, btag, accountid, catid ,partnerid, amount , memo, transferid from CashTrans

TrxRegelm:
- LF weg :
UPDATE TrxRegelm SET memo=REPLACE(memo,Char(10), ' ')
select _id as id, btag, accountid, catid, partnerid, memo, amount, transferid, count as cnt, intervallid, last , isUltimo from TrxRegelm
