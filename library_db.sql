PGDMP  /                    |           database_libreria    16.3    16.3 "               0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false                       0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false                       0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false                       1262    24576    database_libreria    DATABASE     �   CREATE DATABASE database_libreria WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Italian_Italy.1252';
 !   DROP DATABASE database_libreria;
                postgres    false            T           1247    24584    genres    TYPE     �  CREATE TYPE public.genres AS ENUM (
    'FANTASCIENZA',
    'FANTASY',
    'ROMANZO_STORICO',
    'THRILLER',
    'HORROR',
    'ROMANTICO',
    'GIALLO',
    'AVVENTURA',
    'FANTASY_URBANO',
    'FANTASY_EPICO',
    'FAVOLA',
    'FANTASY_SCIENTIFICO',
    'DRAMMATICO',
    'BIOGRAFICO',
    'SAGGIO',
    'SAGGIO_FILOSOFICO',
    'POESIA',
    'COMMEDIA',
    'SATIRA',
    'AZIONE',
    'SCONOSCIUTO'
);
    DROP TYPE public.genres;
       public          postgres    false            ]           1247    24682    lendleasestatus    TYPE     T   CREATE TYPE public.lendleasestatus AS ENUM (
    'on lendlease',
    'delivered'
);
 "   DROP TYPE public.lendleasestatus;
       public          postgres    false            �            1255    24703 #   add_bag(integer, character varying)    FUNCTION     �   CREATE FUNCTION public.add_bag(id_user integer, isbn character varying) RETURNS void
    LANGUAGE plpgsql
    AS $$

BEGIN

    -- Inserisci nel carrello
    INSERT INTO bag (isbn, id_user)
    VALUES (isbn, id_user);
    

END;
$$;
 G   DROP FUNCTION public.add_bag(id_user integer, isbn character varying);
       public          postgres    false            �            1255    24701    check_and_return_users()    FUNCTION     �  CREATE FUNCTION public.check_and_return_users() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
    total_copies INTEGER;
    lendlease_count INTEGER;
    id_users INT[];
    message TEXT;
BEGIN
    -- Ottieni il numero totale di copie per il libro
    SELECT totcopies INTO total_copies FROM public."Books" WHERE isbn = NEW.isbn;

    -- Ottieni il conteggio dei prestiti per il libro
    SELECT COUNT(*) INTO lendlease_count FROM public."LendLease" WHERE isbn = NEW.isbn;

    -- Se il conteggio dei prestiti è uguale al numero totale di copie
    IF lendlease_count = total_copies THEN
        -- Ottieni gli id_user che hanno quel dato isbn nel bag
        SELECT ARRAY_AGG(id_user) INTO id_users FROM public."Bag" WHERE isbn = NEW.isbn;

        -- Costruisci il messaggio con gli id_user
        message := 'Id users with all copies of book on lendlease: ' || array_to_string(id_users, ',');

        -- Invia il messaggio al client
        RAISE INFO '%', message;
    END IF;

    RETURN NEW;
END;
$$;
 /   DROP FUNCTION public.check_and_return_users();
       public          postgres    false            �            1255    24704 "   delivered_lendlease(integer, text)    FUNCTION     |  CREATE FUNCTION public.delivered_lendlease(id_user_param integer, isbn_param text) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
BEGIN
    -- Aggiorna lo stato del prestito in "delivered"
    UPDATE LendLease
    SET status = true
    WHERE id_user = id_user_param AND isbn = isbn_param;

    -- Restituisce TRUE se almeno una riga è stata aggiornata
    RETURN FOUND;
END;
$$;
 R   DROP FUNCTION public.delivered_lendlease(id_user_param integer, isbn_param text);
       public          postgres    false            �            1255    24705    get_overdue_active_users()    FUNCTION     �  CREATE FUNCTION public.get_overdue_active_users() RETURNS SETOF integer
    LANGUAGE plpgsql
    AS $$
DECLARE
    overdue_users INT[];
BEGIN
    -- Seleziona gli id_user con due_datetime passata e status "active"
    SELECT ARRAY_AGG(id_user) INTO overdue_users
    FROM LendLease
    WHERE due_datetime < CURRENT_TIMESTAMP AND status = 'active';

    -- Restituisci l'insieme di id_user
    RETURN QUERY SELECT unnest(overdue_users);
END;
$$;
 1   DROP FUNCTION public.get_overdue_active_users();
       public          postgres    false            �            1255    24706 +   order_lendlease(integer, character varying)    FUNCTION     �  CREATE FUNCTION public.order_lendlease(id_user integer, isbn character varying) RETURNS void
    LANGUAGE plpgsql
    AS $$
DECLARE
    creation_time TIMESTAMP;
    due_time TIMESTAMP;
BEGIN
    -- Calcola il tempo di creazione e di scadenza
    creation_time := NOW();
    due_time := NOW() + INTERVAL '14 days'; -- Assumendo un periodo di prestito di 14 giorni, puoi modificare questo valore secondo necessità
    
    -- Inserisci il nuovo prestito
    INSERT INTO lendlease (isbn, id_user, creation_datetime, due_datetime)
    VALUES (isbn, id_user, creation_time, due_time);
    
    -- Elimina tutte le voci nel carrello per l'utente
    DELETE FROM bag WHERE id_user = id_user;
END;
$$;
 O   DROP FUNCTION public.order_lendlease(id_user integer, isbn character varying);
       public          postgres    false            �            1255    24707 &   remove_bag(integer, character varying)    FUNCTION     �   CREATE FUNCTION public.remove_bag(id_user integer, isbn character varying) RETURNS void
    LANGUAGE plpgsql
    AS $_$
BEGIN
    -- Rimuovi dal carrello
    DELETE FROM bag WHERE isbn = $2 AND id_user = $1;
END;
$_$;
 J   DROP FUNCTION public.remove_bag(id_user integer, isbn character varying);
       public          postgres    false            �            1255    24702    update_copies_on_lendlease()    FUNCTION     $  CREATE FUNCTION public.update_copies_on_lendlease() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    -- Incrementa il conteggio delle copie in prestito per il libro
    UPDATE Book
    SET copiesonlendlease = copiesonlendlease + 1
    WHERE isbn = NEW.isbn;

    RETURN NEW;
END;
$$;
 3   DROP FUNCTION public.update_copies_on_lendlease();
       public          postgres    false            �            1259    24627    Books    TABLE     �   CREATE TABLE public."Books" (
    title character varying(50),
    authors character varying(100),
    image_url character varying(250),
    totcopies integer,
    copiesonlendlease integer,
    isbn integer NOT NULL,
    genre character varying
);
    DROP TABLE public."Books";
       public         heap    postgres    false            �            1259    24687 	   LendLease    TABLE     �   CREATE TABLE public."LendLease" (
    id_user integer,
    creation_datetime timestamp without time zone,
    due_datetime timestamp without time zone,
    id integer NOT NULL,
    delivered boolean DEFAULT false,
    isbn integer NOT NULL
);
    DROP TABLE public."LendLease";
       public         heap    postgres    false            �            1259    24668    bag    TABLE     Q   CREATE TABLE public.bag (
    isbn character varying(13),
    id_user integer
);
    DROP TABLE public.bag;
       public         heap    postgres    false            �            1259    24709    lendlease_id_seq    SEQUENCE     �   CREATE SEQUENCE public.lendlease_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 '   DROP SEQUENCE public.lendlease_id_seq;
       public          postgres    false    219                       0    0    lendlease_id_seq    SEQUENCE OWNED BY     G   ALTER SEQUENCE public.lendlease_id_seq OWNED BY public."LendLease".id;
          public          postgres    false    220            �            1259    24656    users_id_seq    SEQUENCE     }   CREATE SEQUENCE public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 2147483647
    CACHE 1;
 #   DROP SEQUENCE public.users_id_seq;
       public          postgres    false            �            1259    24660    users    TABLE     �   CREATE TABLE public.users (
    id integer DEFAULT nextval('public.users_id_seq'::regclass) NOT NULL,
    password character varying(200) NOT NULL,
    email character varying(50) NOT NULL
);
    DROP TABLE public.users;
       public         heap    postgres    false    216            k           2604    24710    LendLease id    DEFAULT     n   ALTER TABLE ONLY public."LendLease" ALTER COLUMN id SET DEFAULT nextval('public.lendlease_id_seq'::regclass);
 =   ALTER TABLE public."LendLease" ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    220    219                      0    24627    Books 
   TABLE DATA           g   COPY public."Books" (title, authors, image_url, totcopies, copiesonlendlease, isbn, genre) FROM stdin;
    public          postgres    false    215   �1                 0    24687 	   LendLease 
   TABLE DATA           d   COPY public."LendLease" (id_user, creation_datetime, due_datetime, id, delivered, isbn) FROM stdin;
    public          postgres    false    219   �1       
          0    24668    bag 
   TABLE DATA           ,   COPY public.bag (isbn, id_user) FROM stdin;
    public          postgres    false    218   2       	          0    24660    users 
   TABLE DATA           4   COPY public.users (id, password, email) FROM stdin;
    public          postgres    false    217   )2                  0    0    lendlease_id_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('public.lendlease_id_seq', 1, false);
          public          postgres    false    220                       0    0    users_id_seq    SEQUENCE SET     ;   SELECT pg_catalog.setval('public.users_id_seq', 1, false);
          public          postgres    false    216            n           2606    24721    Books Books_isbn_pk 
   CONSTRAINT     W   ALTER TABLE ONLY public."Books"
    ADD CONSTRAINT "Books_isbn_pk" PRIMARY KEY (isbn);
 A   ALTER TABLE ONLY public."Books" DROP CONSTRAINT "Books_isbn_pk";
       public            postgres    false    215            p           2606    24667    users Users_email_key 
   CONSTRAINT     S   ALTER TABLE ONLY public.users
    ADD CONSTRAINT "Users_email_key" UNIQUE (email);
 A   ALTER TABLE ONLY public.users DROP CONSTRAINT "Users_email_key";
       public            postgres    false    217            r           2606    24665    users Users_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY public.users
    ADD CONSTRAINT "Users_pkey" PRIMARY KEY (id);
 <   ALTER TABLE ONLY public.users DROP CONSTRAINT "Users_pkey";
       public            postgres    false    217            t           2606    24715    LendLease lendlease_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public."LendLease"
    ADD CONSTRAINT lendlease_pkey PRIMARY KEY (id);
 D   ALTER TABLE ONLY public."LendLease" DROP CONSTRAINT lendlease_pkey;
       public            postgres    false    219            w           2620    24708 #   LendLease lendlease_created_trigger    TRIGGER     �   CREATE TRIGGER lendlease_created_trigger AFTER INSERT ON public."LendLease" FOR EACH ROW EXECUTE FUNCTION public.update_copies_on_lendlease();
 >   DROP TRIGGER lendlease_created_trigger ON public."LendLease";
       public          postgres    false    219    221            u           2606    24671    bag bag_id_user_fkey    FK CONSTRAINT     s   ALTER TABLE ONLY public.bag
    ADD CONSTRAINT bag_id_user_fkey FOREIGN KEY (id_user) REFERENCES public.users(id);
 >   ALTER TABLE ONLY public.bag DROP CONSTRAINT bag_id_user_fkey;
       public          postgres    false    217    218    4722            v           2606    24691     LendLease lendlease_id_user_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public."LendLease"
    ADD CONSTRAINT lendlease_id_user_fkey FOREIGN KEY (id_user) REFERENCES public.users(id);
 L   ALTER TABLE ONLY public."LendLease" DROP CONSTRAINT lendlease_id_user_fkey;
       public          postgres    false    217    219    4722               Q   x�s+J��N�+.I����M,�T�H��I��LC���*H�4�4�4�0041012�ts�q��v�t��t�t������ G�            x������ � �      
      x������ � �      	      x������ � �     