
-- POPOLAZIONE UTENTE

insert into Utente values ('Ros98','123','Claudia','Rossi','Paziente','F');
insert into Utente values ('Ciccio','100','Alessio','Matteini','Psicologo','M');
insert into Utente values ('Giacomino','1998','Giacomo','Polidori','Paziente','M');
insert into Utente values ('BelPaolino','Q8TT-23!','Paolino','Perfettino','Psicologo','M');
insert into Utente values ('IlPrincipe','JDK90?','Carlo','Terzo','Psicologo','M');
insert into Utente values ('Luigi@67','<pallino>','Luigi','Serafini','Paziente','M');
insert into Utente values ('Gargamella','IPuffi','Fabio','Barchesi','Paziente','M');
insert into Utente values ('IlSole','Teletubbies','Valerio','De Angelis','Paziente','M');
insert into Utente values ('Mars','-','Mario','Bros','Psicologo','M');
insert into Utente values ('Arya','1992','Arya','Padovani','Psicologo','F');
insert into Utente values ('Susy','Aa0-!','Susan','Giostri','Paziente','F');
insert into Utente values ('Sph97','19!?37','Sophie','Cristalli','Psicologo','F');
insert into Utente values ('_Venere@_','ven4578','Venere','Corti','Paziente','F');
insert into Utente values ('Ginny_98','16789','Ginevra','Cristanti','Paziente','F');
insert into Utente values ('Jo_92','pp079!@','Giorgia','Palermi','Paziente','F');



--POPOLAZIONE PAZIENTE
insert into Paziente values(29,'CPTRO93H45G881W',NULL,'Ros98',NULL);
insert into Paziente values(35,'CLIRO90K45T201J','Alcolismo di secondo grado','Giacomino','Ciccio');
insert into Paziente values(35,'LGIMI67W90T631L','Depressione Acuta','Luigi@67','Mars');
insert into Paziente values(27,'PUFFI78T22J542K',NULL,'Gargamella',NULL);
insert into Paziente values(27,'SLPTT50H37J292K','Comportamento aggressivo passivo','IlSole','Mars');
insert into Paziente values(31,'SPH92H7DB99SAP9',NULL,'Susy','Sph97');
insert into Paziente values(28,'ODH8412J3O09SF95','Attacchi di panico','_Venere@_','Ciccio');
insert into Paziente values(27,'UGXP99L8SF9BMD8',NULL,'Ginny_98','Ciccio');
insert into Paziente values(31,'UDHP852RT7NAHG9','Isolamento sociale','Jo_92','Ciccio');



--POPOLAZIONE PSICOLOGO
insert into Psicologo values(25,'Arco della Libertà','Ciccio');
insert into Psicologo values(50,'Derivazione Costante','BelPaolino');
insert into Psicologo values(120,'Il REGNO dei sogni','IlPrincipe');
insert into Psicologo values(38,'Marsolandia','Mars');
insert into Psicologo values(37,'Aria Fresca','Arya');
insert into Psicologo values(32,'Soffia il Vento','Sph97');

--APPUNTAMENTI PSICOLOGO

--ciccio
insert into appuntamento values('1','2024-07-23','15:30','Giacomino','Ciccio');
insert into appuntamento values('2','2024-10-2','9:45','Luigi@67','Ciccio');

--TEST PSICOLOGICI

--ciccio
INSERT INTO testpsicologico VALUES ('2024-05-10', 0, 'Ciccio', '_Venere@_', 'Test di Memoria',1)








