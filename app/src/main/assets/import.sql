Cat:
select _id as id, name, description, obercatid, supercatid, catclassid,
 incomecat, ausgeblendet, cnt from Type_Cat

Account:
select _id as id, _id as cat, name,inhaber,currency, iban, blz, bezeichnung,
0 as creditlimit, verrechnungskonto, kontotyp, openDate
from Account

WPStamm:
select _id as id, _id as partnerid, wpname as name, wpkenn, isin, wptyp, risiko, isBeobachten as beobachten,
ertragestimated as estEarning
from WPStammdaten

WPKurs:
select _id as id, btag, wpid, kurs
from WPKurs

Cashtrx:
- LF weg :
UPDATE CashTrans SET memo=REPLACE(memo,Char(10), ' ')
select _id as id, btag, accountid, catid ,partnerid, amount , memo, transferid from CashTrans

