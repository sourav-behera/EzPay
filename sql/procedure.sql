SET SERVEROUT ON;

-- Get transaction status by transaction id
CREATE OR REPLACE PROCEDURE getTransactionStatusById(id VARCHAR2)
AS
    tr_status transaction_tab.status%TYPE;
BEGIN
    SELECT status INTO tr_status
    FROM transaction_tab t  
    WHERE t.transaction_id = id;
    -- DBMS_OUTPUT.PUT_LINE('Status of the transaction: ' || tr_status);
END getTransactionStatusById;
/


-- Get all transactions
CREATE OR REPLACE PROCEDURE getTransactions
AS
    -- Cursor to select all records from transaction_tab
    CURSOR tr_cursor IS
        SELECT * FROM transaction_tab;

    -- Use %ROWTYPE based on the correct table
    tr_rec transaction_tab%ROWTYPE;
BEGIN
    OPEN tr_cursor;
    LOOP
        FETCH tr_cursor INTO tr_rec;
        EXIT WHEN tr_cursor%NOTFOUND;

        -- Print each column from the transaction record
        DBMS_OUTPUT.PUT_LINE('Transaction ID   : ' || tr_rec.transaction_id);
	DBMS_OUTPUT.PUT_LINE('Transaction type : ' || tr_rec.type);
        DBMS_OUTPUT.PUT_LINE('Amount           : ' || tr_rec.amount);
        DBMS_OUTPUT.PUT_LINE('Status           : ' || tr_rec.status);
	DBMS_OUTPUT.PUT_LINE('Date             : ' || TO_CHAR(tr_rec.transaction_date, 'YYYY-MM-DD'));
        -- Add more lines here if the table has additional columns

        DBMS_OUTPUT.PUT_LINE('-------------------------------');
    END LOOP;
    CLOSE tr_cursor;

EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END getTransactions;
/

-- Get all transactions by type
CREATE OR REPLACE PROCEDURE getTransactionsByType(p_type VARCHAR2)
AS
    CURSOR tr_cursor IS
    SELECT * FROM transaction_tab WHERE type = p_type;

    tr_rec transaction_tab%ROWTYPE;
BEGIN
    OPEN tr_cursor;
    LOOP
        FETCH tr_cursor INTO tr_rec;
        EXIT WHEN tr_cursor%NOTFOUND;

        DBMS_OUTPUT.PUT_LINE('Transaction ID   : ' || tr_rec.transaction_id);
        DBMS_OUTPUT.PUT_LINE('Transaction Type : ' || tr_rec.type);
        DBMS_OUTPUT.PUT_LINE('Amount           : ' || tr_rec.amount);
        DBMS_OUTPUT.PUT_LINE('Status           : ' || tr_rec.status);
        DBMS_OUTPUT.PUT_LINE('Date             : ' || TO_CHAR(tr_rec.transaction_date, 'YYYY-MM-DD'));
        DBMS_OUTPUT.PUT_LINE('-------------------------------');
    END LOOP;
    CLOSE tr_cursor;

EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END getTransactionsByType;
/

-- Get all transaction by status
CREATE OR REPLACE PROCEDURE getTransactionsByStatus(p_status VARCHAR2)
AS
    CURSOR tr_cursor IS
    SELECT * FROM transaction_tab WHERE status = p_status;

    tr_rec transaction_tab%ROWTYPE;
BEGIN
    OPEN tr_cursor;
    LOOP
        FETCH tr_cursor INTO tr_rec;
        EXIT WHEN tr_cursor%NOTFOUND;

        DBMS_OUTPUT.PUT_LINE('Transaction ID   : ' || tr_rec.transaction_id);
        DBMS_OUTPUT.PUT_LINE('Transaction Type : ' || tr_rec.type);
        DBMS_OUTPUT.PUT_LINE('Amount           : ' || tr_rec.amount);
        DBMS_OUTPUT.PUT_LINE('Status           : ' || tr_rec.status);
        DBMS_OUTPUT.PUT_LINE('Date             : ' || TO_CHAR(tr_rec.transaction_date, 'YYYY-MM-DD'));
        DBMS_OUTPUT.PUT_LINE('-------------------------------');
    END LOOP;
    CLOSE tr_cursor;

EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END getTransactionsByStatus;
/

-- Get transactions by date
CREATE OR REPLACE PROCEDURE getTransactionsByDate(p_date DATE)
AS
    CURSOR tr_cursor IS
        SELECT * FROM transaction_tab
        WHERE TRUNC(transaction_date) = TRUNC(p_date);
    tr_rec transaction_tab%ROWTYPE;
BEGIN
    OPEN tr_cursor;
    LOOP
        FETCH tr_cursor INTO tr_rec;
        EXIT WHEN tr_cursor%NOTFOUND;

        DBMS_OUTPUT.PUT_LINE('Transaction ID   : ' || tr_rec.transaction_id);
        DBMS_OUTPUT.PUT_LINE('Transaction Type : ' || tr_rec.type);
        DBMS_OUTPUT.PUT_LINE('Amount           : ' || tr_rec.amount);
        DBMS_OUTPUT.PUT_LINE('Status           : ' || tr_rec.status);
        DBMS_OUTPUT.PUT_LINE('Date             : ' || TO_CHAR(tr_rec.transaction_date, 'YYYY-MM-DD'));
        DBMS_OUTPUT.PUT_LINE('-------------------------------');
    END LOOP;
    CLOSE tr_cursor;

EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END getTransactionsByDate;
/

-- Get transactions within date range
CREATE OR REPLACE PROCEDURE getTransactionsByDateRange(
    p_start_date IN DATE,
    p_end_date   IN DATE
)
AS
    CURSOR tr_cursor IS
        SELECT * FROM transaction_tab
        WHERE TRUNC(transaction_date) BETWEEN TRUNC(p_start_date) AND TRUNC(p_end_date);

    tr_rec transaction_tab%ROWTYPE;
BEGIN
    OPEN tr_cursor;
    LOOP
        FETCH tr_cursor INTO tr_rec;
        EXIT WHEN tr_cursor%NOTFOUND;

        DBMS_OUTPUT.PUT_LINE('Transaction ID   : ' || tr_rec.transaction_id);
        DBMS_OUTPUT.PUT_LINE('Transaction Type : ' || tr_rec.type);
        DBMS_OUTPUT.PUT_LINE('Amount           : ' || tr_rec.amount);
        DBMS_OUTPUT.PUT_LINE('Status           : ' || tr_rec.status);
        DBMS_OUTPUT.PUT_LINE('Date             : ' || TO_CHAR(tr_rec.transaction_date, 'YYYY-MM-DD'));
        DBMS_OUTPUT.PUT_LINE('-------------------------------');
    END LOOP;
    CLOSE tr_cursor;

EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END getTransactionsByDateRange;
/
