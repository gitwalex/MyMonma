Cat:
select _id as id, name, description, obercatid, supercatid, catclassid,
 incomecat, ausgeblendet, cnt from Type_Cat

Account:
select _id as id, _id as cat, name,inhaber,currency, iban, blz, bezeichnung,
"0.00" as creditlimit, verrechnungskonto, kontotyp, openDate, "0.00" as openamount
from Account

WPStamm:
select _id as id, _id as partnerid, wpname as name, wpkenn, isin, wptyp, risiko, isBeobachten as beobachten,
case
when ertragestimated = 0 then "0.00"
when ertragestimated < 100 then "0." ||   substr("00" || ertragestimated, -2)
else substr(ertragestimated, 1, length(ertragestimated) -2 )   || "." ||   substr("00" || ertragestimated, -2)
end as estEarning
from WPStammdaten

WPKurs:
select _id as id, btag, wpid,
case
when kurs = 0 then "0.00"
when kurs < 100 then "0." ||   substr("00" || kurs, -2)
else substr(kurs, 1, length(kurs) -2 )   || "." ||   substr("00" || kurs, -2) end as kurs
from WPKurs

Cashtrx:
- LF weg :
UPDATE CashTrans SET memo=REPLACE(memo,Char(10), ' ')
select _id as id, btag, accountid, catid ,partnerid, amount /100.0 as amount , memo, transferid from CashTrans

