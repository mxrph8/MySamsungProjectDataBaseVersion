package dik.mysamsungproject;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.List;

import dik.mysamsungproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    ArrayList<University> filteredList = new ArrayList<>();
    List<University> sortedUniversities = new ArrayList<>();

    private List<University> universities;

    private static final MutableLiveData<Integer> seekBarValue;
    private static final MutableLiveData<Boolean> cb_fm;
    private static final MutableLiveData<Boolean> cb_se;
    private static final MutableLiveData<Boolean> cb_xb;
    private static final MutableLiveData<Boolean> cb_mi;

    static {
        seekBarValue = new MutableLiveData<>();
        seekBarValue.postValue(400);
        cb_fm = new MutableLiveData<>();
        cb_se = new MutableLiveData<>();
        cb_xb = new MutableLiveData<>();
        cb_mi = new MutableLiveData<>();
    }
    public static MutableLiveData<Integer> getSeekBarValue(){
        return seekBarValue;
    }
    public static MutableLiveData<Boolean> getCb_fm(){
        return cb_fm;
    }
    public static MutableLiveData<Boolean> getCb_se(){
        return cb_se;
    }
    public static MutableLiveData<Boolean> getCb_xb(){
        return cb_xb;
    }

    public static MutableLiveData<Boolean> getCb_mi(){
        return cb_mi;
    }




    FragmentCategory fragmentCategory = new FragmentCategory();

    int CLickSchet = 0;
    String SelectUniversity_fm;
    String SelectUniversity_xb;
    String SelectUniversity_se;
    String SelectUniversity_mi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        arrayAdapterStart();
        initLiveData();
        initView();

    }
    public void initView(){
        Button categorybt = (Button) findViewById(R.id.categorybt);
        categorybt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CLickSchet += 1;
                setNewFragment(fragmentCategory);
            }
        });

        binding.ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDescriptionDialog(position);
            }
        });

    }



    public void initLiveData(){



        seekBarValue.observe(this,
                new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        FilterByPoints();
                    }
                }
                );
        cb_mi.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (getCb_mi().getValue() == true){
                    sortByFaculty("Мат-Инф");
                    SelectUniversity_mi ="Мат-Инф";
                }
                else {
                    unSortByFaculty("Мат-Инф");
                    cleanArrayList();
                }
            }
        });

        cb_fm.observe(this,
                new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if (getCb_fm().getValue() == true){
                            sortByFaculty("Физ-Мат");
                            SelectUniversity_fm ="Физ-Мат";
                        }
                        else {
                            unSortByFaculty("Физ-Мат");
                            cleanArrayList();
                            }
                    }
                });
        cb_se.observe(this,
                new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if (getCb_se().getValue() == true){
                            sortByFaculty("Соц-Эконом");
                            SelectUniversity_se ="Соц-Эконом";
                        }
                        else {unSortByFaculty("Соц-Эконом");
                            cleanArrayList();}
                    }
                });
        cb_xb.observe(this,
                new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if (getCb_xb().getValue() == true){
                            sortByFaculty("Хим-Био");
                            SelectUniversity_xb ="Хим-Био";
                        }
                        else {unSortByFaculty("Хим-Био");
                            cleanArrayList();
                            }
                    }
                });
    }
    public void cleanArrayList(){
        if (sortedUniversities.isEmpty()) {
            UniversityAdapter universitiesAdapter = new UniversityAdapter(this, universities);
            binding.ListView.setAdapter(universitiesAdapter);
        }
    }
    public void FilterByPoints(){
        for (University university : universities) {
            if (university.getPoints() > getSeekBarValue().getValue()) {
            if ((SelectUniversity_fm ==  null ||SelectUniversity_se ==  null || SelectUniversity_xb ==  null ||SelectUniversity_mi ==  null) || (
                    university.getFaculty().equals(SelectUniversity_fm) || university.getFaculty().equals(SelectUniversity_xb) || university.getFaculty().equals(SelectUniversity_se)||university.getFaculty().equals(SelectUniversity_mi) )
                    ){
                sortedUniversities.remove(university);
            }
                UniversityAdapter universitiesAdapter = new UniversityAdapter(this, sortedUniversities);
                binding.ListView.setAdapter(universitiesAdapter);
            }
        }
        }


    private void setNewFragment(Fragment fragment) {
        if (CLickSchet % 2 != 0) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_layout, fragment);
            ft.show(fragment);
            ft.commit();
        } else {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.hide(fragment);
            ft.commit();
        }

    }


    public void arrayAdapterStart() {
        universities = new ArrayList<>();
        universities.add(new University(1, "Университет Синергия", "Хим-Био", 230, "Университет «Синерги́я» (Московский финансово-промышленный университет «Синергия») — российское частное высшее учебное заведение, основанное в 1995 году в Москве. Юридическое наименование — Негосударственное образовательное частное учреждение высшего образования «Московский финансово-промышленный университет „Синергия“»."));
        universities.add(new University(2, "Национальный исследовательский университет «МЭИ» ", "Физ-Мат", 142, "МЭИ — один из крупнейших технических университетов России в области энергетики, электроники, информатики, электротехники. Вуз гордится развитой научной инфраструктурой, среди которой выделяют конструкторское бюро и опытный завод, уникальную ТЭЦ, крупнейшую научно-техническую библиотеку в стране. Выпускники МЭИ в настоящее время работают во всех крупнейших компаниях России."));
        universities.add(new University(3, "Национальный исследовательский университет Высшая школа экономики", "Соц-Эконом", 200, "Национа́льный иссле́довательский университе́т «Вы́сшая шко́ла эконо́мики» (НИУ ВШЭ; «Вы́шка») — автономное учреждение, федеральное государственное высшее учебное заведение. ВШЭ создана в 1992 году, нынешний статус носит с 2009 года. Основной кампус находится в Москве, ещё три — в Санкт-Петербурге, Нижнем Новгороде и Перми."));
        universities.add(new University(4, "Московский политехнический университет", "Хим-Био", 130, "Федеральное государственное автономное образовательное учреждение высшего образования «Московский политехнический университет» (сокращ. «Московский политех»)[1] — высшее учебное заведение в Москве. Имеет филиалы в Ивантеевке, п. Тучково, Чебоксарах, Коломне, Рязани, Электростали."));
        universities.add(new University(5, "Санкт-Петербургский национальный исследовательский Академический университет имени Ж.И. Алферова Российской академии наук", "Физ-Мат", 160, "Федеральное государственное бюджетное учреждение высшего образования и науки «Санкт-Петербургский национальный исследовательский Академический университет имени Ж. И. Алфёрова Российской академии наук» (СПбАУ, Алфёровский университет) — образовательная организация высшего образования в Санкт-Петербурге, единственный в России университет, основанный нобелевским лауреатом."));
        universities.add(new University(6, "Финансовый университет при Правительстве РФ", "Соц-Эконом", 172, "Финансовый университет при Правительстве Российской Федерации — высшее учебное заведение в Москве. Создано в 1919 году. Одно из старейших в России, готовящих экономистов, финансистов, менеджеров, юристов по налоговому, гражданскому и финансовому праву. Также выпускает ИТ-специалистов, социологов, философов и политологов."));
        universities.add(new University(7, "Московский институт психоанализа", "Хим-Био", 109, "Московский институт психоанализа — ведущий психологический вуз России, делает акцент на практико-ориентированное обучение: более 150 баз практик и стажировок. Преподаватели — доктора и кандидаты наук, известные практикующие психологи. Наше преимущество в сочетании классического фундаментального образования с современными тренинговыми и супервизорскими практиками. Входит в ведущие российские рейтинги. По данным Коммерсант.ру, занимает I место в рейтинге зарплат выпускников направления Психология"));
        universities.add(new University(8, "Московский политехнический университет ", "Физ-Мат", 132, "Московский политехнический университет — современный, динамично развивающийся, один из крупнейших вузов России, который ведёт свою историю с 1865 года. Университет выпускает инженеров по широкому спектру направлений, в том числе специалистов в сфере информационных технологий (IT), биотехнологии, интеллектуальной энергетики, беспилотного транспорта и машиностроения. Также здесь представлены и гуманитарные, социально-экономические, творческие направления."));
        universities.add(new University(9, "Южный федеральный университет", "Соц-Эконом", 123, "Ю́жный федера́льный университе́т (ЮФУ) — высшее учебное заведение, один из федеральных университетов России, расположенный в Ростове-на-Дону и Таганроге Ростовской области[1]. Является крупным научно-образовательным центром России."));
        universities.add(new University(10, "Московский государственный областной университет", "Хим-Био", 140, "Госуда́рственный университе́т просвеще́ния (ГУП) — высшее учебное заведение в Москве и Московской области. Основан в 1931 году. В 1957—1991 годы — Московский областной педагогический институт (МОПИ) имени Н. К. Крупской."));
        universities.add(new University(11, "Национальный исследовательский ядерный университет «МИФИ»", "Физ-Мат", 158, "Национа́льный иссле́довательский я́дерный университет «МИФИ́» (Московский инженерно-физический институт) — один из первых двух национальных исследовательских университетов России (наряду с МИСиС), образован 8 апреля 2009 года на базе Московского инженерно-физического института (государственного университета)."));
        universities.add(new University(12, "Сибирский федеральный университет", "Соц-Эконом", 112, "Сиби́рский федера́льный университе́т (СФУ) — высшее учебное заведение, расположенное в Красноярске. Первый в России федеральный университет[8]. Крупный научно-исследовательский и образовательный центр в России. Крупнейший университет восточной части России."));
        universities.add(new University(13, "Уральский государственный лесотехнический университет", "Физ-Мат", 130, "dadadaff"));
        universities.add(new University(14, "Уральский ГАУ", "Физ-Мат", 132, "УрГАУ — это бывшая Уральская Государственная Сельскохозяйственная Академия. За 75 лет выпущено более 32 тысяч специалистов. Среди выпускников университета есть Герои Социалистического труда, более сотни выпускников удостоены Почетных званий, более 10 человек стали лауреатами Государственных премий. В настоящее время УрГАУ представляет крупное многопрофильное высшее учебное заведение, готовящее специалистов для всех отраслей агропромышленного комплекса."));
        universities.add(new University(15, "Уральский государственный горный университет", "Физ-Мат", 137, "Уральский государственный горный университет – это первый вуз Урала. В настоящее время – это единственный в мире полнопрофильный горный университет и единственный горный университет в России.Уральский государственный горный университет признан лауреатом конкурса Федеральной службы по надзору в сфере образования и науки «Системы качества подготовки выпускников образовательных учреждений профессионального образования»(2008 г.). В 2011 году университет стал лауреатом конкурса «100 лучших вузов России» в номинации «Европейское качество»"));
        universities.add(new University(16, "Уральский институт Государственной противопожарной службы МЧС России", "Физ-Мат", 111, "В институте готовят специалистов (бюджетная форма обучения) для замещения должностей среднего и старшего начальствующего состава для Государственной противопожарной службы МЧС России, подлежащих комплектованию специалистами с высшим профессиональным образованием."));
        universities.add(new University(17, "Российский новый университет", "Мат-Инф", 168, "Российский новый университет имеет государственный статус классического университета. Признан эффективным вузом по результатам мониторингов Министерства образования и науки РФ, а также входит в список Лучших вузов мира (MosIUR). В университете представлены все основные направления высшего образования — для абитуриентов открыто более 50 программ бакалавриата и специалитета, 19 программ магистратуры. В состав РосНОУ входят 5 институтов, колледж и 6 филиалов. Университет осуществляет активную международную деятельность, а именно: заключены договоры с 27 странами мира для прохождения практики и стажировок студентами, а также участвует в организации совместных научно-образовательных проектов, международных конференций, семинаров."));
        universities.add(new University(18, "Волго-Вятский филиал МТУСИ", "Мат-Инф", 215, "Волго-Вятский филиал является структурным подразделением ордена Трудового Красного Знамени федеральное государственное бюджетное образовательное учреждение высшего образования «Московский технический университет связи и информатики»."));
        universities.add(new University(19, "Московский технический университет связи и информатики", "Мат-Инф", 222, "ТУСИ является ведущим отраслевым техническим университетом в области телекоммуникаций, информационных технологий и информационной безопасности. Выпускники МТУСИ работают в ведущих российских и международных телекоммуникационных и ИТ-компаниях, таких как Яндекс, Фирма «1С», Лаборатория Касперского, Крок, ТТЦ «Останкино», Ростелеком, МТС, Мегафон, Российские космические системы и др. Факультет Информационных технологий МТУСИ входит в ТОП-10 по востребованности выпускников на рынке труда согласно рейтингу факультетов московских вузов."));
        universities.add(new University(20, "Институт международных экономических связей", "Мат-Инф", 240, "ИМЭС – это бизнес–институт международного уровня, выпускающий высококвалифицированных специалистов, уровень которых соответствует современным требованиям в области международного бизнеса, менеджмента, государственной службы, психологии, юриспруденции, рекламы, педагогики. Наше конкурентное преимущество перед другими вузами состоит в том, что мы – «камерный» вуз. Благодаря этому особое внимание уделяется качеству обучения и индивидуальному подходу к каждому студенту."));
        universities.add(new University(21, "Российский национальный исследовательский медицинский университет имени Н. И. Пирогова", "Мат-Инф", 171, "РНИМУ им. Н.И. Пирогова – ведущий научно-образовательный медицинский центр, обеспечивающий подготовку врачей (специальности – лечебное дело, педиатрия, стоматология), провизоров, клинических психологов, социальных работников, а также уникальных медицинских научных кадров (специальности – медицинская биохимия, медицинская кибернетика, медицинская биофизика). Университет активно участвует в реализации концепции непрерывного образования, предлагая образовательные услуги по подготовке абитуриентов на подготовительных курсах, обучение по программам бакалавриата, специалитета, подготовки кадров высшей квалификации, дополнительное профессиональное образование. По программам высшего профессионального образования в Университете ежегодно обучается более 12 000 человек и более 10 000 человек – по программам дополнительного профессионального образования."));
        universities.add(new University(22, "Российский химико-технологический университет им. Д.И. Менделеева", "Мат-Инф", 260, "Лидер рейтинга химико-технологических вузов страны. У университета беспрецедентно высокие контрольные цифры приема на головную специальность «Химическая технология»: 470 мест. На базе вуза проходит межрегиональная химическая олимпиада школьников имени академика П.Д. Саркисова для учащихся 9-11-х классов."));
        universities.add(new University(23, "Гжельский государственный университет", "Мат-Инф", 240, "Гжельский государственный университет (ГГУ) представляет собой стремительно развивающийся многопрофильный вуз. В стенах института обучаются студенты со всей России, осваивая востребованные на рынке труда профессии. Количество желающих обучаться в ГГУ растет из года в год. За последние три года количество студентов ГГУ увеличилось в три раза и составило 2850 человек, из которых 1293 являются студентами очной формы обучения. Преподавательский состав колледжа — 58 человек. Профессорско-преподавательский состав университета — 72 человека, из которых 14 человек являются докторами наук и 35 имеют ученую степень кандидата наук. "));
        universities.add(new University(24, "Российский университет кооперации", "Мат-Инф", 235, "Российский университет кооперации — главный учебный и научный центр системы кооперативного образования России, вуз со столетней историей и сложившимися классическими традициями образования. Учредитель Университета — Центросоюз Российской Федерации. В Университете эффективно действует система непрерывного образования, которая включает в себя довузовскую подготовку, начальное, среднее, высшее профессиональное образование, повышение квалификации и переподготовку специалистов, обучение в аспирантуре и докторантуре. Вуз выдает документы об образовании государственного образца."));
        universities.add(new University(25, "Московский технологический институт", "Мат-Инф", 250, "18 ноября 2021года Московский открытый институт официально сменил название на Московский технологический институт (МТИ). Вуз взял курс на современные технологии и активно пополняет свою линейку техническими направлениями."));
        universities.add(new University(26, "Национальный исследовательский университет ИТМО", "Мат-Инф", 250, "Университет ИТМО – ведущий вуз России в области информационных и фотонных технологий, один из немногих российских вузов, получивших в 2009 году статус национального исследовательского университета. В составе Университета ИТМО функционируют более 20 институтов и факультетов, где обучаются около 12 тысяч студентов и аспирантов, работают 1200 преподавателей и научных сотрудников (из них около 700 – доктора и кандидаты наук)."));
        universities.add(new University(27, "Университет ИТМО ", "Физ-Мат", 111, "dadadaff"));
        universities.add(new University(28, "Уральский институт Государственной противопожарной службы МЧС России", "Физ-Мат", 240, "Университет ИТМО – ведущий вуз России в области информационных и фотонных технологий, один из немногих российских вузов, получивших в 2009 году статус национального исследовательского университета. В составе Университета ИТМО функционируют более 20 институтов и факультетов, где обучаются около 12 тысяч студентов и аспирантов, работают 1200 преподавателей и научных сотрудников (из них около 700 – доктора и кандидаты наук)."));
        universities.add(new University(29, "Российский государственный социальный университет", "Соц-Эконом", 200, "Российский государственный социальный университет (РГСУ) - это один из ведущих российских вузов, который готовит успешных и квалифицированных специалистов в самых разных сферах экономики, политики и общественной жизни страны. История вуза насчитывает больше века. Университет является идейным наследником Коммунистического университета имени Я.М.Свердлова (1919) и Высшей партийной школы при ЦК ВКП(б). Сегодня на 11 факультетах РГСУ получают образование более 19 тысяч студентов из 65 стран мира. Подготовка ведется по 187 основным образовательным программам бакалавриата, специалитета, магистратуры и аспирантуры. Со студентами работают высококвалифицированные преподаватели, 85% из них имеют ученую степень. В образовательном процессе широко используются инновационные подходы, цифровые технологии и инструменты удаленного доступа к образовательному контенту. Университет известен в стране качеством реализуемой воспитательной работы."));
        universities.add(new University(30, "Московский государственный медико-стоматологический университет", "Хим-Био", 140, "Занимает 4-е место в рейтинге 97 профильных вузов. В 2012 году на дневном отделении по направлению «Стоматология» было заявлено 230 бюджетных мест, что позволило поступить в вуз абитуриентам со средним баллом за один ЕГЭ 72,3 пункта. Соответствующие показатели специальности «Лечебное дело» – 210 мест и 71 балл. Кстати, по этому направлению активно ведется набор на условиях целевого приема: в 2012 году по этой системе студентами вуза стали 147 человек."));


        UniversityAdapter universitiesAdapter = new UniversityAdapter(this, universities);
        binding.ListView.setAdapter(universitiesAdapter);
    }


    public void sortUniversitiesByFaculty(List<University> universities, String faculty) {
        if (faculty == null || faculty.isEmpty()) {
            return;
        }

        for (University university : universities) {
            if (university.getFaculty().equals(faculty)) {
                sortedUniversities.add(university);
            }
        }

    }


    private void sortByFaculty(String faculty) {
        sortUniversitiesByFaculty(universities, faculty);
        UniversityAdapter universitiesAdapter = new UniversityAdapter(this, sortedUniversities);
        binding.ListView.setAdapter(universitiesAdapter);
    }

    public void unSortUniversitiesByFaculty(List<University> universities, String faculty) {
        if (faculty == null || faculty.isEmpty()) {
            return;
        }

        for (University university : universities) {
            if (university.getFaculty().equals(faculty)) {
                sortedUniversities.remove(university);
            }

        }

    }

    private void unSortByFaculty(String faculty) {
        unSortUniversitiesByFaculty(universities, faculty);
        UniversityAdapter universitiesAdapter = new UniversityAdapter(this, sortedUniversities);
        binding.ListView.setAdapter(universitiesAdapter);
    }


    private void showDescriptionDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(universities.get(position).getName())
                .setMessage(universities.get(position).getDescription())
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    }

