Cat:
select _id as id, name, description, obercatid, supercatid, catclassid,
case incomecat
when 0 then 0
when 1 then 1
else 0
end as incomecat, ausgeblendet, cnt from Type_Cat

Account:
select _id as id, _id as cat, name,inhaber,currency, iban, blz, bezeichnung,
"0.00" as creditlimit, verrechnungskonto, kontotyp, openDate, "0.00" as openamount
from Account