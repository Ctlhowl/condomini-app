CREATE SCHEMA condomini_app;

set search_path to condomini_app;


CREATE TABLE condomini_app.condominium(
    id SERIAL PRIMARY KEY,
    "name" VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL UNIQUE,
    last_year_balance REAL NOT NULL DEFAULT 0
);

CREATE TABLE condomini_app.apartment(
    id SERIAL PRIMARY KEY,
    owner VARCHAR(255) NOT NULL,
    tenant VARCHAR(255),
    scala VARCHAR(255) NOT NULL,
    last_year_balance REAL NOT NULL DEFAULT 0,
    mill_tab_A REAL NOT NULL DEFAULT 0,
    mill_tab_B REAL NOT NULL DEFAULT 0,
    mill_tab_C REAL NOT NULL DEFAULT 0,
    mill_tab_D REAL NOT NULL DEFAULT 0,
    condominium_id INTEGER NOT NULL,

    CONSTRAINT fk_condominium_id FOREIGN KEY (condominium_id) REFERENCES condomini_app.condominium(id) ON DELETE CASCADE
);

CREATE TABLE condomini_app.table_appendix(
    id SERIAL PRIMARY KEY,
    category VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL
);


CREATE TABLE condomini_app.quote(
    id SERIAL PRIMARY KEY,
    total_amount REAL NOT NULL DEFAULT 0,
    condominium_id INTEGER NOT NULL,
    table_id INTEGER NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),

    CONSTRAINT fk_table_id FOREIGN KEY (table_id) REFERENCES condomini_app.table_appendix(id) ON DELETE cascade,
    CONSTRAINT fk_condominium_id FOREIGN KEY (condominium_id) REFERENCES condomini_app.condominium(id) ON DELETE CASCADE
);


CREATE TABLE condomini_app.outlay(
    id SERIAL PRIMARY KEY,
    amount REAL NOT NULL DEFAULT 0,
    description VARCHAR(255) NOT null,
    outlay_type VARCHAR(255) NOT NULL,
    operation_type VARCHAR(255) NOT NULL,
    payment_method VARCHAR(255) NOT NULL,
    condominium_id INTEGER NOT NULL,
    apartment_id INTEGER,
    table_id INTEGER NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),

    CONSTRAINT fk_apartment_id FOREIGN KEY (apartment_id) REFERENCES condomini_app.apartment(id) ON DELETE CASCADE,
    CONSTRAINT fk_table_id FOREIGN KEY (table_id) REFERENCES condomini_app.table_appendix(id) ON DELETE CASCADE,
    CONSTRAINT fk_condominium_id FOREIGN KEY (condominium_id) REFERENCES condomini_app.condominium(id) ON DELETE CASCADE
);


-- Valori iniziali tabelle


-- Descrizione: Alla creazione di un nuovo condominio crea un preventivo a 0 per ogni tabella 
CREATE OR REPLACE FUNCTION init_quote()
RETURNS TRIGGER
AS $$
    DECLARE
    table REFCURSOR;
    table_id int;
    BEGIN
        OPEN table FOR SELECT id FROM condomini_app.table_appendix;
        LOOP
            FETCH table INTO table_id;
            EXIT WHEN NOT FOUND;

            INSERT INTO condomini_app.quote(id, total_amount, condominium_id, table_id, created_at) VALUES (nextval('condomini_app.quote_id_seq'), 0, new.id, table_id, now()); 
        END LOOP;
        
        return new;
END;
$$ language plpgsql;

CREATE OR REPLACE TRIGGER trigger_init_quote
AFTER INSERT ON condomini_app.condominium
FOR EACH ROW
EXECUTE FUNCTION init_quote();
