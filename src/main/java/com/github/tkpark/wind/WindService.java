package com.github.tkpark.wind;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
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

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class WindService {

    @Autowired
    CommonService commonService;

    private final WindRepository windRepository;

    private final RoadPointRepository roadPointRepository;

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

    public WindService(WindRepository windRepository, RoadPointRepository roadPointRepository) {
        this.windRepository = windRepository;
        this.roadPointRepository = roadPointRepository;
    }

    @Transactional(readOnly = true)
    public List<Wind> findAllByBaseDateAndBaseTime(String baseDate, String baseTime) {
        return windRepository.findAllByBaseDateAndBaseTime(baseDate, baseTime);
    }

    @Transactional(readOnly = true)
    public List<Wind> findAll() {
        return windRepository.findAll();
    }

    @Transactional
    public String save(String date, String time) {
        log.info("WindService.save(), date:{}, time:{}", date, time);

        List<RoadPoint> roadList = roadPointRepository.findAllByRequestYnOrderByNxAscNyAsc("Y");
        log.debug("roadList.size():{}", roadList.size());

        for(RoadPoint road : roadList) {
            log.debug("road:{}, roadPoint:{}, roadNm:{}, nx:{}, ny:{}", road.getRoad(), road.getRoadPoint(), road.getRoadNm(), road.getNx(), road.getNy());

            ArrayList<Wind> list = new ArrayList<>();
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
                    .queryParam("nx", road.getNx())
                    .queryParam("ny", road.getNy())
                    .build();

            try {
                resEntity = commonService.request(builder.toUri(), HttpMethod.GET, reqEntity, new ParameterizedTypeReference<String>() {});
                log.info("res:{}", resEntity.getBody());

                XmlMapper mapper = new XmlMapper();
                UltraSrtNcst.Response res = mapper.readValue(resEntity.getBody(), UltraSrtNcst.Response.class);
                log.info("ResultCode:{}, ResultMsg:{}", res.getHeader().getResultCode(), res.getHeader().getResultMsg());

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
                        // +900이상, –900 이하 값은 Missing 값으로 처리
                        // 관측장비가 없는 해양 지역이거나 관측장비의 결측 등으로 자료가 없음을 의미
                        switch(item.getCategory()) {
                            case "PTY":
                                pty = item.getObsrValue();
                                break;
                            case "REH":
                                reh = item.getObsrValue();
                                if(Integer.parseInt(reh) >= 900 || Integer.parseInt(reh) <= -900) {
                                    reh = "0";
                                }
                                break;
                            case "RN1":
                                rn1 = item.getObsrValue();
                                break;
                            case "T1H":
                                t1h = item.getObsrValue();
                                break;
                            case "UUU":
                                uuu = item.getObsrValue();
                                break;
                            case "VEC":
                                vec = item.getObsrValue();
                                wd16 = calcWindDirection16(vec);
                                break;
                            case "VVV":
                                vvv = item.getObsrValue();
                                break;
                            case "WSD":
                                wsd = item.getObsrValue();
                                break;
                        }
                    }
                    list.add(new Wind(baseDate, baseTime, nx, ny, pty, reh, rn1, t1h, uuu, vec, vvv, wsd, wd16,"bacth", null));
                }
            } catch(Exception e) {
                log.error("e:", e);
            }

            for(Wind wind : list) {
                windRepository.save(wind);
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