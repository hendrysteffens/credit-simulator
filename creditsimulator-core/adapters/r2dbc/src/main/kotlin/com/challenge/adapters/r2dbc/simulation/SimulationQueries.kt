package com.challenge.adapters.r2dbc.simulation

object SimulationQueries {
    fun insertSimulation() =
        """
            INSERT INTO "simulation" (
                simulation_id,
                request_id,
                status,
                birthdate,
                amount_of_months,
                currency,
                loan_amount,
                total_amount,
                monthly_payment,
                total_interest,
                created_at
            )
            VALUES (
                :simulation_id,
                :request_id,
                :status,
                :birthdate,
                :amount_of_months,
                :currency,
                :loan_amount,
                :total_amount,
                :monthly_payment,
                :total_interest,
                :created_at
            )
        """

    fun selectSimulations() =
        """
            SELECT
                request_id,
                birthdate,
                amount_of_months,
                currency,
                loan_amount,
                total_amount,
                monthly_payment,
                total_interest,
                created_at
            FROM "simulation"
                where 1 = 1
        """

    fun whereRequestId() =
        """
            AND "request_id" = (:request_id)
        """

    fun countSimulations() =
        """
            SELECT 
                count(request_id) as total
            FROM
                "simulation"
            WHERE
                1 = 1
        """

    const val ADD_LIMIT = """
        LIMIT :limit
    """

    const val ADD_OFFSET = """
       OFFSET :offset
    """

    fun sortingBy(
        field: String,
        asc: Boolean = true,
    ) = """
        ORDER BY $field ${if (asc) "ASC" else "DESC"}
        """
}
