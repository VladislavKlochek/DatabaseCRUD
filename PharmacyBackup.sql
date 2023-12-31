PGDMP         %                {            Pharmacy    15.3    15.3 3    9           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            :           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            ;           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            <           1262    16480    Pharmacy    DATABASE     �   CREATE DATABASE "Pharmacy" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'English_United States.1252';
    DROP DATABASE "Pharmacy";
                postgres    false            �            1259    16510 
   ingredient    TABLE     �   CREATE TABLE public.ingredient (
    id integer NOT NULL,
    nameofingredient character varying(60) NOT NULL,
    priceofingredient money NOT NULL
);
    DROP TABLE public.ingredient;
       public         heap    postgres    false            �            1259    16509    ingredient_id_seq    SEQUENCE     �   CREATE SEQUENCE public.ingredient_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 (   DROP SEQUENCE public.ingredient_id_seq;
       public          postgres    false    219            =           0    0    ingredient_id_seq    SEQUENCE OWNED BY     G   ALTER SEQUENCE public.ingredient_id_seq OWNED BY public.ingredient.id;
          public          postgres    false    218            �            1259    16516    ingredientdrug    TABLE     Y   CREATE TABLE public.ingredientdrug (
    idofdrug integer,
    idofingredient integer
);
 "   DROP TABLE public.ingredientdrug;
       public         heap    postgres    false            �            1259    16497    listofdrugs    TABLE     �   CREATE TABLE public.listofdrugs (
    id integer NOT NULL,
    nameofdrug character varying(30) NOT NULL,
    manufactured boolean DEFAULT false,
    priceofdrug money NOT NULL,
    idoftype integer
);
    DROP TABLE public.listofdrugs;
       public         heap    postgres    false            �            1259    16496    listofdrugs_id_seq    SEQUENCE     �   CREATE SEQUENCE public.listofdrugs_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 )   DROP SEQUENCE public.listofdrugs_id_seq;
       public          postgres    false    217            >           0    0    listofdrugs_id_seq    SEQUENCE OWNED BY     I   ALTER SEQUENCE public.listofdrugs_id_seq OWNED BY public.listofdrugs.id;
          public          postgres    false    216            �            1259    16561    listofdrugsinstorage    TABLE     j   CREATE TABLE public.listofdrugsinstorage (
    idofdrug integer,
    amountinstorage integer DEFAULT 0
);
 (   DROP TABLE public.listofdrugsinstorage;
       public         heap    postgres    false            �            1259    16544    listoforders    TABLE     �   CREATE TABLE public.listoforders (
    id integer NOT NULL,
    idofdrug integer,
    idofrecipe integer,
    done boolean DEFAULT false,
    orderacceptancetime timestamp without time zone,
    phonenumber character varying(60)
);
     DROP TABLE public.listoforders;
       public         heap    postgres    false            �            1259    16543    listoforders_id_seq    SEQUENCE     �   CREATE SEQUENCE public.listoforders_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.listoforders_id_seq;
       public          postgres    false    224            ?           0    0    listoforders_id_seq    SEQUENCE OWNED BY     K   ALTER SEQUENCE public.listoforders_id_seq OWNED BY public.listoforders.id;
          public          postgres    false    223            �            1259    16530    recipe    TABLE     �  CREATE TABLE public.recipe (
    id integer NOT NULL,
    amountofdrug integer DEFAULT 0,
    usagemethod character varying(30),
    patientdiagnosis character varying(30) NOT NULL,
    patientfullname character varying(50) NOT NULL,
    doctorfullname character varying(50) NOT NULL,
    patientage smallint,
    idofdrug integer,
    CONSTRAINT recipe_patientage_check CHECK ((patientage > 0))
);
    DROP TABLE public.recipe;
       public         heap    postgres    false            �            1259    16529    recipe_id_seq    SEQUENCE     �   CREATE SEQUENCE public.recipe_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 $   DROP SEQUENCE public.recipe_id_seq;
       public          postgres    false    222            @           0    0    recipe_id_seq    SEQUENCE OWNED BY     ?   ALTER SEQUENCE public.recipe_id_seq OWNED BY public.recipe.id;
          public          postgres    false    221            �            1259    16490 
   typeofdrug    TABLE     i   CREATE TABLE public.typeofdrug (
    id integer NOT NULL,
    drugtype character varying(30) NOT NULL
);
    DROP TABLE public.typeofdrug;
       public         heap    postgres    false            �            1259    16489    typeofdrug_id_seq    SEQUENCE     �   CREATE SEQUENCE public.typeofdrug_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 (   DROP SEQUENCE public.typeofdrug_id_seq;
       public          postgres    false    215            A           0    0    typeofdrug_id_seq    SEQUENCE OWNED BY     G   ALTER SEQUENCE public.typeofdrug_id_seq OWNED BY public.typeofdrug.id;
          public          postgres    false    214            �            1259    16575    typeofdrug_id_seq1    SEQUENCE     �   ALTER TABLE public.typeofdrug ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.typeofdrug_id_seq1
    START WITH 7
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    215            �           2604    16513    ingredient id    DEFAULT     n   ALTER TABLE ONLY public.ingredient ALTER COLUMN id SET DEFAULT nextval('public.ingredient_id_seq'::regclass);
 <   ALTER TABLE public.ingredient ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    219    218    219            �           2604    16500    listofdrugs id    DEFAULT     p   ALTER TABLE ONLY public.listofdrugs ALTER COLUMN id SET DEFAULT nextval('public.listofdrugs_id_seq'::regclass);
 =   ALTER TABLE public.listofdrugs ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    217    216    217            �           2604    16547    listoforders id    DEFAULT     r   ALTER TABLE ONLY public.listoforders ALTER COLUMN id SET DEFAULT nextval('public.listoforders_id_seq'::regclass);
 >   ALTER TABLE public.listoforders ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    223    224    224            �           2604    16533 	   recipe id    DEFAULT     f   ALTER TABLE ONLY public.recipe ALTER COLUMN id SET DEFAULT nextval('public.recipe_id_seq'::regclass);
 8   ALTER TABLE public.recipe ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    221    222    222            /          0    16510 
   ingredient 
   TABLE DATA           M   COPY public.ingredient (id, nameofingredient, priceofingredient) FROM stdin;
    public          postgres    false    219   :       0          0    16516    ingredientdrug 
   TABLE DATA           B   COPY public.ingredientdrug (idofdrug, idofingredient) FROM stdin;
    public          postgres    false    220   @       -          0    16497    listofdrugs 
   TABLE DATA           Z   COPY public.listofdrugs (id, nameofdrug, manufactured, priceofdrug, idoftype) FROM stdin;
    public          postgres    false    217   �A       5          0    16561    listofdrugsinstorage 
   TABLE DATA           I   COPY public.listofdrugsinstorage (idofdrug, amountinstorage) FROM stdin;
    public          postgres    false    225   �D       4          0    16544    listoforders 
   TABLE DATA           h   COPY public.listoforders (id, idofdrug, idofrecipe, done, orderacceptancetime, phonenumber) FROM stdin;
    public          postgres    false    224   �E       2          0    16530    recipe 
   TABLE DATA           �   COPY public.recipe (id, amountofdrug, usagemethod, patientdiagnosis, patientfullname, doctorfullname, patientage, idofdrug) FROM stdin;
    public          postgres    false    222   �F       +          0    16490 
   typeofdrug 
   TABLE DATA           2   COPY public.typeofdrug (id, drugtype) FROM stdin;
    public          postgres    false    215   K       B           0    0    ingredient_id_seq    SEQUENCE SET     @   SELECT pg_catalog.setval('public.ingredient_id_seq', 94, true);
          public          postgres    false    218            C           0    0    listofdrugs_id_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('public.listofdrugs_id_seq', 55, true);
          public          postgres    false    216            D           0    0    listoforders_id_seq    SEQUENCE SET     B   SELECT pg_catalog.setval('public.listoforders_id_seq', 21, true);
          public          postgres    false    223            E           0    0    recipe_id_seq    SEQUENCE SET     <   SELECT pg_catalog.setval('public.recipe_id_seq', 21, true);
          public          postgres    false    221            F           0    0    typeofdrug_id_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('public.typeofdrug_id_seq', 1, true);
          public          postgres    false    214            G           0    0    typeofdrug_id_seq1    SEQUENCE SET     A   SELECT pg_catalog.setval('public.typeofdrug_id_seq1', 26, true);
          public          postgres    false    226            �           2606    16515    ingredient ingredient_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public.ingredient
    ADD CONSTRAINT ingredient_pkey PRIMARY KEY (id);
 D   ALTER TABLE ONLY public.ingredient DROP CONSTRAINT ingredient_pkey;
       public            postgres    false    219            �           2606    16503    listofdrugs listofdrugs_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.listofdrugs
    ADD CONSTRAINT listofdrugs_pkey PRIMARY KEY (id);
 F   ALTER TABLE ONLY public.listofdrugs DROP CONSTRAINT listofdrugs_pkey;
       public            postgres    false    217            �           2606    16550    listoforders listoforders_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.listoforders
    ADD CONSTRAINT listoforders_pkey PRIMARY KEY (id);
 H   ALTER TABLE ONLY public.listoforders DROP CONSTRAINT listoforders_pkey;
       public            postgres    false    224            �           2606    16537    recipe recipe_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY public.recipe
    ADD CONSTRAINT recipe_pkey PRIMARY KEY (id);
 <   ALTER TABLE ONLY public.recipe DROP CONSTRAINT recipe_pkey;
       public            postgres    false    222            �           2606    16495    typeofdrug typeofdrug_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public.typeofdrug
    ADD CONSTRAINT typeofdrug_pkey PRIMARY KEY (id);
 D   ALTER TABLE ONLY public.typeofdrug DROP CONSTRAINT typeofdrug_pkey;
       public            postgres    false    215            �           2606    16582    listofdrugsinstorage idofdrug    FK CONSTRAINT     �   ALTER TABLE ONLY public.listofdrugsinstorage
    ADD CONSTRAINT idofdrug FOREIGN KEY (idofdrug) REFERENCES public.listofdrugs(id) ON DELETE CASCADE;
 G   ALTER TABLE ONLY public.listofdrugsinstorage DROP CONSTRAINT idofdrug;
       public          postgres    false    217    225    3214            �           2606    16587    recipe idofdrug    FK CONSTRAINT     �   ALTER TABLE ONLY public.recipe
    ADD CONSTRAINT idofdrug FOREIGN KEY (idofdrug) REFERENCES public.listofdrugs(id) ON DELETE SET NULL;
 9   ALTER TABLE ONLY public.recipe DROP CONSTRAINT idofdrug;
       public          postgres    false    222    3214    217            �           2606    16597    listoforders idofdrug    FK CONSTRAINT     �   ALTER TABLE ONLY public.listoforders
    ADD CONSTRAINT idofdrug FOREIGN KEY (idofdrug) REFERENCES public.listofdrugs(id) ON DELETE CASCADE;
 ?   ALTER TABLE ONLY public.listoforders DROP CONSTRAINT idofdrug;
       public          postgres    false    3214    217    224            �           2606    16602    ingredientdrug idofdrug    FK CONSTRAINT     �   ALTER TABLE ONLY public.ingredientdrug
    ADD CONSTRAINT idofdrug FOREIGN KEY (idofdrug) REFERENCES public.listofdrugs(id) ON DELETE CASCADE;
 A   ALTER TABLE ONLY public.ingredientdrug DROP CONSTRAINT idofdrug;
       public          postgres    false    217    220    3214            �           2606    16592    listoforders idofrecipe    FK CONSTRAINT     �   ALTER TABLE ONLY public.listoforders
    ADD CONSTRAINT idofrecipe FOREIGN KEY (idofrecipe) REFERENCES public.recipe(id) ON DELETE CASCADE;
 A   ALTER TABLE ONLY public.listoforders DROP CONSTRAINT idofrecipe;
       public          postgres    false    3218    222    224            �           2606    16577    listofdrugs idoftype    FK CONSTRAINT     �   ALTER TABLE ONLY public.listofdrugs
    ADD CONSTRAINT idoftype FOREIGN KEY (idoftype) REFERENCES public.typeofdrug(id) ON DELETE SET NULL;
 >   ALTER TABLE ONLY public.listofdrugs DROP CONSTRAINT idoftype;
       public          postgres    false    217    3212    215            �           2606    16607    ingredientdrug ingredient    FK CONSTRAINT     �   ALTER TABLE ONLY public.ingredientdrug
    ADD CONSTRAINT ingredient FOREIGN KEY (idofingredient) REFERENCES public.ingredient(id) ON DELETE CASCADE;
 C   ALTER TABLE ONLY public.ingredientdrug DROP CONSTRAINT ingredient;
       public          postgres    false    220    219    3216            /   �  x��W�r�V<�}>$U��؁�ǈ���dI�r����J��*j������B*J�`�"�����Y��G��N���_����f�j~�.��>|��W�	�Z�a���}��o����s��-VtY�����Z����78��W�I4w���?�p��O��jp�����8q�ҙ��<�x�C�U��4u��`���AL�Y�Ĥ,\>���Z��Sw0�d� 9����3�Yn�l���s%�5���O���(b��o肀]��,w���).i*��I�9�/�W�r�(\8��_c��,�ą���`��]�u���HF�{�A�5�$w! ���~�+#x&t����ݥܐ��(�.�� Lw�8Bl3C��א+<ʵ�i�t+1_�@`ϥ�|�?���? �)~pM<z�J��P�	�M�;��H���,����B�&�E����DI��M%o҃�I�g���M�5j�LV�5@<���c`�j(�E�	O8�4UՂ�&S ��3�����@���1��E%��R��Rϵ�Z��0�'�c��]���\M�0]t5vq$qX&"�����uۉ����͒��}HP��������=sqbT�l�Ɍ�0<�-���)�,e^��oQ�u�#�ھ������WS�N@aVm�bA}���1�z���$�u��ވ
O�}%	�@]�Y6V�셵�-ᮘ+nYJu�cߺ�4	h�K����^�-���{�D�M�?�G�%�@�k��MY���)dm�E��fr��&�k�$�v�)���6����5����RךҤR0�2)�갆Е����k��O�i7�4���&���F"��@:�Bn�W��'�闆]O��K��1/+ŗy�&V�r�P��M����0`�&Z��{��<��K���>C"[����%w9��A���>I:SYQu��p���ذ�u�v;
�T��∨�����]X�LA�_�;��iP76�� �ɘ�n���Ŵ������e ���Y��q��w}� �Q�o���޶����/99v��V�z�2qY<l*Ɖ���`P=P]�7��i&�[�Ļ�[�>f�f�>
��,|���%��+���?�94R|VHڵ�G��[(���\���e�'U��II��m���ښF>�u�в[�ݵ.`5V/2.�5�����n���pYoȣ�\`�޳u4TR�{�.���am�J����d�'�f`ݘ� f���#����b��y����F�m��.���B�Wv���M��#%��c)~�G./uw���n:h+�{C����� �āP�r�%tϙ<��Xe�CE�3hiS��k_�U��4��}[���t�F�)0��*��E5m�E/�]E�����Y����t��������Ru��W	���VM�:l��T�W�v��O�!����+n�ׁ~�?PH9���ܕ!��3���4�����ɋ���(re,j���������9�/�� �      0   Y  x�%�ɑ�0D�3`̄���/���u�W	��Ö�u{Z�{�l5�Loˀ�j߶�Pōc!r�z~-.�Ե��������]X)aYy���~�\�WZ�u乾�N�;u���}e���m�r�Z���d��AS�ǧ� -xڃ�J���'Y��H��5�Sz!�Ԅmw���t�Y�H��}$�C������(>U���I}�I1�UH��\	%�#2M�I�ۓ�B�w$�l%�9p��DA33	6�����2���=!9է��v$�Z�I�H�L�=59U*'�_MgiM�F�[9!�Er���J�����*'$'$�z�	���Z�)'$'��^:D�!��{�uϬHT��
9!���^~y�      -   `  x�]U�NA<�~��G;��w�9F�p�D8)��+؀"�!�C�DQnH������/��Q�jvm+B<��n����~�������'�o�\׺�쿈���k���c�b?���6	]���"�&��ۨ�|]��~�Y��U�U~"�G<��/	�2�e���z@\��~U�3�؉�
�$��w�p�~%�qy���+����8���]E{�#[���P��g�X9E��zU�)JM�M3EK㿠v�ʉx�T�yl9���0c3u�����I΢}�e�'�T]��Cu���n��T6��B9�U*���N_A�>�ʪ�B��AJ�p�#Lw�CP���OܦA�\1d���Pʠ���qK-h�$����$x���0@Ŷ(��E�G�z	hx�� ��|�����-sդ[��ómO� ~��Ӧ���&�g�#��TJbđ=��9Ϙ�<���/Z���守�bL�zK��-:� ��=
��+%t9h�/�iEk@�*e-�0yr���@)��I[��)!�ǥ�9�#kR�h����G��&��D�[�Wh�E�N��7�c��R �s�T�v�L��f�n��#�L�����X�`��j8�zl�g�֗`��W!�1{_7��B��h�9�9x��`s��Q��@��i�յXC���ߐ!2��bX��&r�s�Y�f�y��47ᄮ�6����[�dF�R=�ܘ�v���;D�-v�B�q���z�qm� zl�Qn{�T��룣Wo:h��)Ñ�s�\��a�ֲ��5����=�LﴟO�]�TD3���ϵ�}7���+=4c��A���J���6�f�K�l��X�m�0�^ΤB�3OwbwSF�mE� �E(�      5   �   x�%��q0�R1� �^���^�AhX
�����`$^���w�a�%F��%q�0��e��ט,Ơ�q��,^7���0��LԹL!�RЊٸc�@i�w<aǃ�:ȼT`CT"�(��Pa�Eo��#>-3�>�2E.��,�T�����p+X�9�Ƥ�u�e�m�ޢ���vb�Q�^~�}����?55J      4   �   x�m��m�0�3UE`��Djk�9�dv��x���硇��:���K�����8�\5�\�;��4�ŕc�&}���a���9c,���Ӵ�y�N�5
��u5Kؒk�MS�z��f*�q���[U
%�67ܹНE�Ұ�W�ǭ����L���bmEҜK.��a{EO�ج{`ǆ��pP"�H�W�S7�
7B��u� n��j^��ө�uWsU�~��%ʳ��-��s��k%xi      2   7  x��WmRG��{�=Aʻ�E�9���M'�N�`P9�L�5����̍��3+-TH$��@�������7�A�O��m��oa���Y��)#3�Oi&�]��))W`9�\i
�1S�	�%�f\�s�枖"s������s��7�.͘�c�:�_#�7K�X��F%I�4	� 	̍=2wf��{�;ԑL=�˜:z!����3�{o{D}��#��@�,s+�W�Cx=u�9��m�.���+6���3>����H~aO�1��{'/�/КٷXO_��9���c�5#�����3�6������2�ÍOv L��i#��0~d���@�K�r��,l/������o�)��(�"XR��z�U��	� H���&��0�c��n��,�[Ds�6�s!9�G�/��ta��� �o*����X~��|-��A��fD��u���Ȣ|��tQeV����L�0���t	����Ft���6�Y_����
��y�%�B����;s|/�q��,�����Ņ��n���5 ����nۀ�j.�0�*^�y�r��1�|J)E�9JA�j���xS]w��ޤ*<˂8�� ޕ�.q��G%��R�� Y����z�0c������V�7�(�R��'ߑ�$q�����{�B��77_�XV�h��T�!.+7&Jb�,8:n�IL�ׂ��O�k;�~Yda���+re��q���V)�0?4im�&�Kh�^=ۢ��R1�Q�������tq���0��-�K����D�c�TL�yʀ^s��2�ߍ9�����:aB��Y+� 8N��w>����륊a
t/�;�hr��w��-'�.�W�Cᨦ&���H��P����:�k$� L�L�j8|���Ev�"_|vf��8٧PY��-����I� �II��Ɋa-��;��w�kDz`
�/ka�%
�U�4�(���֔��V���q[D�9+vp�ix�	��+�/�I��f�k���}�aJdix�=G�|�t���Ӡ�޾.��$��H�@h����WZ��׹*<-��[�?"���}��g_w����A3|�S�� ���      +   `   x�34弰�/쾰�bӅ]vp�s^Xpa��F Ӆ}.vsp^�T����;.����Vah�ya>H5w��1����� ) 7j     