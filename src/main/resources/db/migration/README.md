# PostgreSQL Export

Generated on 2024-10-22 07:47:32.371202265 UTC by Fabricate v1.0.0

## Instructions

To load the data into a PostgreSQL database, execute the following command replacing the values with your own:

```bash
psql postgres://<user>:<password>@<host>:<port>/<db> -f load.sql -v current_dir="$(pwd)"
```

## Exported tables

This is the list of exported tables, with their corresponding row count and file names:

    public.appointment: 100 rows => appointment-bbddfb3df5e4fc5f38ae366d5be9b3ed757540a2095b7ff90ccd5357592f0b11.csv
    public.diagnosis: 100 rows => diagnosis-441e4cd4d03cbb1ef3b2257ca01ddec5c3bb41f93c9c14742c7ea9737407bec3.csv
    public.patient: 100 rows => patient-1495f661381b8e3a557859744433ad371e86540fd5a2a993da7ba9644a80759d.csv
    public.users: 100 rows => users-4b5f19126d0e4e447e223c0b8523ec4d7b0db72306168499b9dbc9aa2184a41a.csv