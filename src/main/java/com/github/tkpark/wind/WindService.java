package com.github.tkpark.wind;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.github.tkpark.data.UltraSrtFcst;
import com.github.tkpark.data.UltraSrtNcst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
@Slf4j
public class WindService {

    @Autowired
    CommonService commonService;

    private final WindRepository windRepository;

    private final RoadPointRepository roadPointRepository;

    private final RoadMasterRepository roadMasterRepository;

    private final WindLocationRepository windLocationRepository;

    private final WindLocationDataRepository windLocationDataRepository;

    private final WindForecastRepository windForecastRepository;

    private final WindForecastDataRepository windForecastDataRepository;

    @Value("${data-api.service-key}")
    private String SERVICE_KEY;

    @Value("${data-api.ultra-srt-ncst}")
    private String ULTRA_SRT_NCST; // 초단기실황조회

    @Value("${data-api.ultra-srt-fcst}")
    private String ULTRA_SRT_FCST; // 초단기예보조회

    @Value("${data-api.vilage-fcst}")
    private String VILAGE_FCST; // 동네예보조회

    @Value("${data-api.fcst-version}")
    private String FCST_VERSION; // 예보버전조회

    public WindService(WindRepository windRepository, RoadPointRepository roadPointRepository
            , RoadMasterRepository roadMasterRepository, WindLocationRepository windLocationRepository
            , WindLocationDataRepository windLocationDataRepository, WindForecastRepository windForecastRepository, WindForecastDataRepository windForecastDataRepository) {
        this.windRepository = windRepository;
        this.roadPointRepository = roadPointRepository;
        this.roadMasterRepository = roadMasterRepository;
        this.windLocationRepository = windLocationRepository;
        this.windLocationDataRepository = windLocationDataRepository;
        this.windForecastRepository = windForecastRepository;
        this.windForecastDataRepository = windForecastDataRepository;
    }

    @Transactional(readOnly = true)
    public List<WindLocationData> findAllWindLocationData() {
        return windLocationDataRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<WindLocationData> findAllWindLocationDataDistance(String latitude, String longitude) {
        return windLocationDataRepository.findAllWindLocationDataDistance(latitude, longitude);
    }

    @Transactional(readOnly = true)
    public List<WindForecastData> findAllWindForecastDataDistance(String latitude, String longitude) {
        return windForecastDataRepository.findAllWindForecastDataDistance(latitude, longitude);
    }

    @Transactional(readOnly = true)
    public List<Wind> findAllByBaseDateAndBaseTime(String baseDate, String baseTime) {
        return windRepository.findAllByBaseDateAndBaseTime(baseDate, baseTime);
    }

    @Transactional(readOnly = true)
    public Wind findByWindData(String baseDate, String baseTime, String nx, String ny) {
        return windRepository.findByBaseDateAndBaseTimeAndNxAndNy(baseDate, baseTime, nx, ny);
    }

    @Transactional(readOnly = true)
    public List<Wind> findAll() {
        return windRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<RoadPoint> findAllRoadPoint() {
        return roadPointRepository.findAllByRequestYnOrderByNxAscNyAsc("Y");
    }

    @Transactional(readOnly = true)
    public List<RoadPoint> findByRoadPointList(String code) {
        return roadPointRepository.findByRoad(code);
    }

    @Transactional(readOnly = true)
    public List<RoadMaster> findAllRoadMaster() {
        return roadMasterRepository.findAllByOrderBySeqAsc();
    }

    @Transactional
    public String windData(String date, String time) {
        log.info("WindService.windData(), date:{}, time:{}", date, time);

        List<WindLocationInterface> windLocationList = windLocationRepository.findAllGroupBy();
        log.debug("windLocationList.size():{}", windLocationList.size());

        for(WindLocationInterface windLocation : windLocationList) {
            log.debug("nx:{}, ny:{}", windLocation.getNx(), windLocation.getNy());

            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
            HttpEntity<MultiValueMap<String, String>> reqEntity = new HttpEntity<>(headers);
            ResponseEntity<String> resEntity = null;

            UriComponents builder = UriComponentsBuilder.fromHttpUrl(ULTRA_SRT_NCST)
                    .queryParam("serviceKey", SERVICE_KEY)
                    .queryParam("pageNo", "1")
                    .queryParam("numOfRows", "10")
                    .queryParam("dataType", "XML")
                    .queryParam("base_date", date)
                    .queryParam("base_time", time)
                    .queryParam("nx", windLocation.getNx())
                    .queryParam("ny", windLocation.getNy())
                    .build();

            try {
                resEntity = commonService.request(builder.toUri(), HttpMethod.GET, reqEntity, new ParameterizedTypeReference<String>() {});
                //log.info("res:{}", resEntity.getBody());

                XmlMapper mapper = new XmlMapper();
                UltraSrtNcst.Response res = mapper.readValue(resEntity.getBody(), UltraSrtNcst.Response.class);
                log.info("resultCode:{}, resultMsg:{}", res.getHeader().getResultCode(), res.getHeader().getResultMsg());

                if(res.getHeader().getResultCode().equals("00") && res.getBody().getItems().size() > 0) {
                    String baseDate = "";
                    String baseTime = "";
                    String nx = "";
                    String ny = "";
                    String pty = "";
                    String reh = "";
                    String rn1 = "";
                    String t1h = "";
                    String uuu = "";
                    String vec = "";
                    String vvv = "";
                    String wsd = "";
                    String wd16 = "";

                    for(UltraSrtNcst.Response.Body.Item item : res.getBody().getItems()) {
                        if(item.getCategory() == null)
                            continue;

                        if(baseDate.equals("")) {
                            baseDate = item.getBaseDate();
                        }
                        if(baseTime.equals("")) {
                            baseTime = item.getBaseTime();
                        }
                        if(nx.equals("")) {
                            nx = String.valueOf(item.getNx());
                        }
                        if(ny.equals("")) {
                            ny = String.valueOf(item.getNy());
                        }

                        float tempValue = Float.parseFloat(item.getObsrValue());
                        // +900이상, –900 이하 값은 Missing 값으로 처리
                        // 관측장비가 없는 해양 지역이거나 관측장비의 결측 등으로 자료가 없음을 의미
                        switch(item.getCategory()) {
                            case "PTY":
                                pty = item.getObsrValue();
                                if(tempValue >= 900.0f || tempValue <= -900.0f) {
                                    pty = "0";
                                }
                                break;
                            case "REH":
                                reh = item.getObsrValue();
                                if(tempValue >= 900.0f || tempValue <= -900.0f) {
                                    reh = "0";
                                }
                                break;
                            case "RN1":
                                rn1 = item.getObsrValue();
                                if(tempValue >= 900.0f || tempValue <= -900.0f) {
                                    rn1 = "0";
                                }
                                break;
                            case "T1H":
                                t1h = item.getObsrValue();
                                if(tempValue >= 900.0f || tempValue <= -900.0f) {
                                    t1h = "error";
                                }
                                break;
                            case "UUU":
                                uuu = item.getObsrValue();
                                if(tempValue >= 900.0f || tempValue <= -900.0f) {
                                    uuu = "0";
                                }
                                break;
                            case "VEC":
                                vec = item.getObsrValue();
                                if(tempValue >= 900.0f || tempValue <= -900.0f) {
                                    vec = "0";
                                }
                                wd16 = calcWindDirection16(vec);
                                break;
                            case "VVV":
                                vvv = item.getObsrValue();
                                if(tempValue >= 900.0f || tempValue <= -900.0f) {
                                    vvv = "0";
                                }
                                break;
                            case "WSD":
                                wsd = item.getObsrValue();
                                if(tempValue >= 900.0f || tempValue <= -900.0f) {
                                    wsd = "0";
                                }
                                break;
                        }
                    }
                    if(!t1h.equals("error")) {
                        windRepository.save(new Wind(baseDate, baseTime, nx, ny, pty, reh, rn1, t1h, uuu, vec, vvv, wsd, wd16,"bacth", null));
                    }
                }
            } catch(Exception e) {
                log.error("e:", e);
            }
        }
        return "Success";
    }

    @Transactional
    public String windForecast(String date, String time) {
        log.info("WindService.windForecast(), date:{}, time:{}", date, time);

        List<WindLocationInterface> windLocationList = windLocationRepository.findAllGroupBy();
        log.debug("windLocationList.size():{}", windLocationList.size());

        for(WindLocationInterface windLocation : windLocationList) {
            log.debug("nx:{}, ny:{}", windLocation.getNx(), windLocation.getNy());

            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
            HttpEntity<MultiValueMap<String, String>> reqEntity = new HttpEntity<>(headers);
            ResponseEntity<String> resEntity = null;

            UriComponents builder = UriComponentsBuilder.fromHttpUrl(ULTRA_SRT_FCST)
                    .queryParam("serviceKey", SERVICE_KEY)
                    .queryParam("pageNo", "1")
                    .queryParam("numOfRows", "60")
                    .queryParam("dataType", "XML")
                    .queryParam("base_date", date)
                    .queryParam("base_time", time)
                    .queryParam("nx", windLocation.getNx())
                    .queryParam("ny", windLocation.getNy())
                    .build();

            try {
                resEntity = commonService.request(builder.toUri(), HttpMethod.GET, reqEntity, new ParameterizedTypeReference<String>() {});
                //log.info("res:{}", resEntity.getBody());

                XmlMapper mapper = new XmlMapper();
                UltraSrtFcst.Response res = mapper.readValue(resEntity.getBody(), UltraSrtFcst.Response.class);
                log.info("resultCode:{}, resultMsg:{}, totalCount:{}", res.getHeader().getResultCode(), res.getHeader().getResultMsg(), res.getBody().getTotalCount());

                int fcstCnt = res.getBody().getTotalCount() / 10;

                WindForecast[] forecastArr = new WindForecast[fcstCnt];

                if(res.getHeader().getResultCode().equals("00") && res.getBody().getItems().size() > 0) {

                    for(int i=0; i<res.getBody().getItems().size(); i++) {
                        UltraSrtFcst.Response.Body.Item item = res.getBody().getItems().get(i);

                        if(item.getCategory() == null)
                            continue;

                        int num = i % fcstCnt;

                        if(forecastArr[num] == null) {
                            String baseDate = item.getBaseDate();
                            String baseTime = item.getBaseTime();
                            String nx = String.valueOf(item.getNx());
                            String ny = String.valueOf(item.getNy());
                            String forecastTime = item.getFcstDate() + item.getFcstTime();

                            forecastArr[num] = new WindForecast(baseDate, baseTime, nx, ny, forecastTime, "bacth");
                        }

                        switch(item.getCategory()) {
                            case "LGT":
                                forecastArr[num].setLgt(item.getFcstValue());
                                break;
                            case "PTY":
                                forecastArr[num].setPty(item.getFcstValue());
                                break;
                            case "RN1":
                                forecastArr[num].setRn1(item.getFcstValue());
                                break;
                            case "SKY":
                                forecastArr[num].setSky(item.getFcstValue());
                                break;
                            case "T1H":
                                forecastArr[num].setT1h(item.getFcstValue());
                                break;
                            case "REH":
                                forecastArr[num].setReh(item.getFcstValue());
                                break;
                            case "UUU":
                                forecastArr[num].setUuu(item.getFcstValue());
                                break;
                            case "VVV":
                                forecastArr[num].setVvv(item.getFcstValue());
                                break;
                            case "VEC":
                                forecastArr[num].setVec(item.getFcstValue());
                                forecastArr[num].setWd16(calcWindDirection16(item.getFcstValue()));
                                break;
                            case "WSD":
                                forecastArr[num].setWsd(item.getFcstValue());
                                break;
                        }
                    }
                    for(int i=0; i<forecastArr.length; i++) {
                        windForecastRepository.save(forecastArr[i]);
                    }
                }
            } catch(Exception e) {
                log.error("e:", e);
            }
        }
        return "Success";
    }

    public String calcWindDirection16(String vec) {
        // 16방위 변환식:(풍향값 + 22.5 * 0.5) / 22.5) = 변환값(소수점 이하 버림)
        String wd16 = "";

        int v = Integer.parseInt(vec);
        int result = (int)((v + 22.5 * 0.5) / 22.5);
        log.debug("calcWindDirection16... vec:{}, result:{}", vec, result);
        switch(result) {
            case 1:
                wd16 = "NNE";
                break;
            case 2:
                wd16 = "NE";
                break;
            case 3:
                wd16 = "ENE";
                break;
            case 4:
                wd16 = "E";
                break;
            case 5:
                wd16 = "ESE";
                break;
            case 6:
                wd16 = "SE";
                break;
            case 7:
                wd16 = "SSE";
                break;
            case 8:
                wd16 = "S";
                break;
            case 9:
                wd16 = "SSW";
                break;
            case 10:
                wd16 = "SW";
                break;
            case 11:
                wd16 = "WSW";
                break;
            case 12:
                wd16 = "W";
                break;
            case 13:
                wd16 = "WNW";
                break;
            case 14:
                wd16 = "NW";
                break;
            case 15:
                wd16 = "NNW";
                break;
            case 0:
            case 16:
                wd16 = "N";
                break;
        }

        return wd16;
    }

}
