/*
 * Copyright 2022 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package data

trait TestXml {

  lazy val CC043A = <CC043A>
    <SynIdeMES1>UNOC</SynIdeMES1>
    <SynVerNumMES2>3</SynVerNumMES2>
    <MesSenMES3>NTA.GB</MesSenMES3>
    <MesRecMES6>alphabetacharliedelta</MesRecMES6>
    <DatOfPreMES9>20210630</DatOfPreMES9>
    <TimOfPreMES10>1319</TimOfPreMES10>
    <IntConRefMES11>alphabeta</IntConRefMES11>
    <MesIdeMES19>alpha</MesIdeMES19>
    <MesTypMES20>GB051B</MesTypMES20>
    <SEAINFSLI>
      <SeaNumSLI2>1</SeaNumSLI2>
      <SEAIDSID>
        <SeaIdeSID1>NCTS001</SeaIdeSID1>
      </SEAIDSID>
    </SEAINFSLI>
    <GOOITEGDS>
      <IteNumGDS7>1</IteNumGDS7>
      <GooDesGDS23>Daffodils</GooDesGDS23>
      <GroMasGDS46>1000</GroMasGDS46>
      <NetMasGDS48>950</NetMasGDS48>
      <RESOFCONROC><ConIndROC1>OR</ConIndROC1>
      </RESOFCONROC>
      <PRODOCDC2>
        <DocTypDC21>tval</DocTypDC21>
        <DocRefDC23>token</DocRefDC23>
        <DocRefDCLNG>to</DocRefDCLNG>
        <ComOfInfDC25>token</ComOfInfDC25>
        <ComOfInfDC25LNG>to</ComOfInfDC25LNG>
      </PRODOCDC2>
      <SPEMENMT2>
        <AddInfMT21>20.22EUR12GBalphabetachar</AddInfMT21>
        <AddInfCodMT23>CAL</AddInfCodMT23>
      </SPEMENMT2>
      <PACGS2>
        <MarNumOfPacGS21>AB234</MarNumOfPacGS21>
        <KinOfPacGS23>BX</KinOfPacGS23>
        <NumOfPacGS24>10</NumOfPacGS24>
      </PACGS2>
    </GOOITEGDS>
  </CC043A>

  lazy val CC043AWithNoDocumentsOrMentions = <CC043A>
    <SynIdeMES1>UNOC</SynIdeMES1>
    <SynVerNumMES2>3</SynVerNumMES2>
    <MesSenMES3>NTA.GB</MesSenMES3>
    <MesRecMES6>alphabetacharliedelta</MesRecMES6>
    <DatOfPreMES9>20210630</DatOfPreMES9>
    <TimOfPreMES10>1319</TimOfPreMES10>
    <IntConRefMES11>alphabeta</IntConRefMES11>
    <MesIdeMES19>alpha</MesIdeMES19>
    <MesTypMES20>GB051B</MesTypMES20>
    <SEAINFSLI>
      <SeaNumSLI2>1</SeaNumSLI2>
      <SEAIDSID>
        <SeaIdeSID1>NCTS001</SeaIdeSID1>
      </SEAIDSID>
    </SEAINFSLI>
    <GOOITEGDS>
      <IteNumGDS7>1</IteNumGDS7>
      <GooDesGDS23>Daffodils</GooDesGDS23>
      <GroMasGDS46>1000</GroMasGDS46>
      <NetMasGDS48>950</NetMasGDS48>
      <RESOFCONROC><ConIndROC1>OR</ConIndROC1>
      </RESOFCONROC>
      <PACGS2>
        <MarNumOfPacGS21>AB234</MarNumOfPacGS21>
        <KinOfPacGS23>BX</KinOfPacGS23>
        <NumOfPacGS24>10</NumOfPacGS24>
      </PACGS2>
    </GOOITEGDS>
  </CC043A>

  lazy val CC051B = <CC051B>
    <SynIdeMES1>UNOC</SynIdeMES1>
    <SynVerNumMES2>3</SynVerNumMES2>
    <MesSenMES3>NTA.GB</MesSenMES3>
    <MesRecMES6>alphabetacharliedelta</MesRecMES6>
    <DatOfPreMES9>20210630</DatOfPreMES9>
    <TimOfPreMES10>1319</TimOfPreMES10>
    <IntConRefMES11>alphabeta</IntConRefMES11>
    <AppRefMES14>NCTS</AppRefMES14>
    <TesIndMES18>0</TesIndMES18>
    <MesIdeMES19>alpha</MesIdeMES19>
    <MesTypMES20>GB051B</MesTypMES20>
    <HEAHEA>
      <RefNumHEA4>alphabetacharlie</RefNumHEA4>
      <DocNumHEA5>alphabetacharlie</DocNumHEA5>
      <TypOfDecHEA24>T1</TypOfDecHEA24>
      <CouOfDesCodHEA30>IT</CouOfDesCodHEA30>
      <CouOfDisCodHEA55>GB</CouOfDisCodHEA55>
      <IdeOfMeaOfTraAtDHEA78>NC15 REG</IdeOfMeaOfTraAtDHEA78>
      <ConIndHEA96>0</ConIndHEA96>
      <DiaLanIndAtDepHEA254>EN</DiaLanIndAtDepHEA254>
      <NCTSAccDocHEA601LNG>EN</NCTSAccDocHEA601LNG>
      <TotNumOfIteHEA305>1</TotNumOfIteHEA305>
      <TotNumOfPacHEA306>10</TotNumOfPacHEA306>
      <TotGroMasHEA307>1000</TotGroMasHEA307>
      <DecDatHEA383>20210630</DecDatHEA383>
      <DecPlaHEA394>Dover</DecPlaHEA394>
      <NoRelMotHEA272>Test</NoRelMotHEA272>
    </HEAHEA>
    <TRAPRIPC1>
      <NamPC17>NCTS UK TEST LAB HMCE</NamPC17>
      <StrAndNumPC122>11TH FLOOR, ALEX HOUSE, VICTORIA AV</StrAndNumPC122>
      <PosCodPC123>SS99 1AA</PosCodPC123>
      <CitPC124>SOUTHEND-ON-SEA, ESSEX</CitPC124>
      <CouPC125>GB</CouPC125>
      <TINPC159>GB954131533000</TINPC159>
    </TRAPRIPC1>
    <TRACONCO1>
      <NamCO17>NCTS UK TEST LAB HMCE</NamCO17>
      <StrAndNumCO122>11TH FLOOR, ALEX HOUSE, VICTORIA AV</StrAndNumCO122>
      <PosCodCO123>SS99 1AA</PosCodCO123>
      <CitCO124>SOUTHEND-ON-SEA, ESSEX</CitCO124>
      <CouCO125>GB</CouCO125>
      <TINCO159>GB954131533000</TINCO159>
    </TRACONCO1>
    <TRACONCE1>
      <NamCE17>NCTS UK TEST LAB HMCE</NamCE17>
      <StrAndNumCE122>ITALIAN OFFICE</StrAndNumCE122>
      <PosCodCE123>IT99 1IT</PosCodCE123>
      <CitCE124>MILAN</CitCE124>
      <CouCE125>IT</CouCE125>
      <TINCE159>IT11ITALIANC11</TINCE159>
    </TRACONCE1>
    <CUSOFFDEPEPT>
      <RefNumEPT1>GB000060</RefNumEPT1>
    </CUSOFFDEPEPT>
    <CUSOFFTRARNS>
      <RefNumRNS1>FR001260</RefNumRNS1>
      <ArrTimTRACUS085>20210630</ArrTimTRACUS085>
    </CUSOFFTRARNS>
    <CUSOFFDESEST>
      <RefNumEST1>IT018105</RefNumEST1>
    </CUSOFFDESEST>
    <CONRESERS>
      <ConDatERS14>20210630</ConDatERS14>
      <ConResCodERS16>B1</ConResCodERS16>
    </CONRESERS>
    <RESOFCON534>
      <DesTOC2>See Header for details</DesTOC2>
      <ConInd424>OT</ConInd424>
    </RESOFCON534>
    <GUAGUA><GuaTypGUA1>1</GuaTypGUA1>
      <GUAREFREF>
        <GuaRefNumGRNREF1>12GBalphabetachar</GuaRefNumGRNREF1>
        <AccCodREF6>AC01</AccCodREF6>
        <VALLIMECVLE>
          <NotValForECVLE1>0</NotValForECVLE1>
        </VALLIMECVLE>
      </GUAREFREF>
    </GUAGUA>
    <GOOITEGDS>
      <IteNumGDS7>1</IteNumGDS7>
      <GooDesGDS23>Daffodils</GooDesGDS23>
      <GroMasGDS46>1000</GroMasGDS46>
      <NetMasGDS48>950</NetMasGDS48>
      <RESOFCONROC><ConIndROC1>OR</ConIndROC1>
      </RESOFCONROC>
      <PACGS2>
        <MarNumOfPacGS21>AB234</MarNumOfPacGS21>
        <KinOfPacGS23>BX</KinOfPacGS23>
        <NumOfPacGS24>10</NumOfPacGS24>
      </PACGS2>
    </GOOITEGDS>
  </CC051B>

  lazy val CC051BWithMultipleGoodsItems = <CC051B>
    <SynIdeMES1>UNOC</SynIdeMES1>
    <SynVerNumMES2>3</SynVerNumMES2>
    <MesSenMES3>NTA.GB</MesSenMES3>
    <MesRecMES6>alphabetacharliedelta</MesRecMES6>
    <DatOfPreMES9>20210630</DatOfPreMES9>
    <TimOfPreMES10>1319</TimOfPreMES10>
    <IntConRefMES11>alphabeta</IntConRefMES11>
    <AppRefMES14>NCTS</AppRefMES14>
    <TesIndMES18>0</TesIndMES18>
    <MesIdeMES19>alpha</MesIdeMES19>
    <MesTypMES20>GB051B</MesTypMES20>
    <HEAHEA>
      <RefNumHEA4>alphabetacharlie</RefNumHEA4>
      <DocNumHEA5>alphabetacharlie</DocNumHEA5>
      <TypOfDecHEA24>T1</TypOfDecHEA24>
      <CouOfDesCodHEA30>IT</CouOfDesCodHEA30>
      <CouOfDisCodHEA55>GB</CouOfDisCodHEA55>
      <IdeOfMeaOfTraAtDHEA78>NC15 REG</IdeOfMeaOfTraAtDHEA78>
      <ConIndHEA96>0</ConIndHEA96>
      <DiaLanIndAtDepHEA254>EN</DiaLanIndAtDepHEA254>
      <NCTSAccDocHEA601LNG>EN</NCTSAccDocHEA601LNG>
      <TotNumOfIteHEA305>1</TotNumOfIteHEA305>
      <TotNumOfPacHEA306>10</TotNumOfPacHEA306>
      <TotGroMasHEA307>1000</TotGroMasHEA307>
      <DecDatHEA383>20210630</DecDatHEA383>
      <DecPlaHEA394>Dover</DecPlaHEA394>
      <NoRelMotHEA272>Test</NoRelMotHEA272>
    </HEAHEA>
    <TRAPRIPC1>
      <NamPC17>NCTS UK TEST LAB HMCE</NamPC17>
      <StrAndNumPC122>11TH FLOOR, ALEX HOUSE, VICTORIA AV</StrAndNumPC122>
      <PosCodPC123>SS99 1AA</PosCodPC123>
      <CitPC124>SOUTHEND-ON-SEA, ESSEX</CitPC124>
      <CouPC125>GB</CouPC125>
      <TINPC159>GB954131533000</TINPC159>
    </TRAPRIPC1>
    <TRACONCO1>
      <NamCO17>NCTS UK TEST LAB HMCE</NamCO17>
      <StrAndNumCO122>11TH FLOOR, ALEX HOUSE, VICTORIA AV</StrAndNumCO122>
      <PosCodCO123>SS99 1AA</PosCodCO123>
      <CitCO124>SOUTHEND-ON-SEA, ESSEX</CitCO124>
      <CouCO125>GB</CouCO125>
      <TINCO159>GB954131533000</TINCO159>
    </TRACONCO1>
    <TRACONCE1>
      <NamCE17>NCTS UK TEST LAB HMCE</NamCE17>
      <StrAndNumCE122>ITALIAN OFFICE</StrAndNumCE122>
      <PosCodCE123>IT99 1IT</PosCodCE123>
      <CitCE124>MILAN</CitCE124>
      <CouCE125>IT</CouCE125>
      <TINCE159>IT11ITALIANC11</TINCE159>
    </TRACONCE1>
    <CUSOFFDEPEPT>
      <RefNumEPT1>GB000060</RefNumEPT1>
    </CUSOFFDEPEPT>
    <CUSOFFTRARNS>
      <RefNumRNS1>FR001260</RefNumRNS1>
      <ArrTimTRACUS085>20210630</ArrTimTRACUS085>
    </CUSOFFTRARNS>
    <CUSOFFDESEST>
      <RefNumEST1>IT018105</RefNumEST1>
    </CUSOFFDESEST>
    <CONRESERS>
      <ConDatERS14>20210630</ConDatERS14>
      <ConResCodERS16>B1</ConResCodERS16>
    </CONRESERS>
    <RESOFCON534>
      <DesTOC2>See Header for details</DesTOC2>
      <ConInd424>OT</ConInd424>
    </RESOFCON534>
    <GUAGUA><GuaTypGUA1>1</GuaTypGUA1>
      <GUAREFREF>
        <GuaRefNumGRNREF1>12GBalphabetachar</GuaRefNumGRNREF1>
        <AccCodREF6>AC01</AccCodREF6>
        <VALLIMECVLE>
          <NotValForECVLE1>0</NotValForECVLE1>
        </VALLIMECVLE>
      </GUAREFREF>
    </GUAGUA>
    <GOOITEGDS>
      <IteNumGDS7>1</IteNumGDS7>
      <GooDesGDS23>Daffodils</GooDesGDS23>
      <GroMasGDS46>1000</GroMasGDS46>
      <NetMasGDS48>950</NetMasGDS48>
      <RESOFCONROC><ConIndROC1>OR</ConIndROC1>
      </RESOFCONROC>
      <PACGS2>
        <MarNumOfPacGS21>AB234</MarNumOfPacGS21>
        <KinOfPacGS23>BX</KinOfPacGS23>
        <NumOfPacGS24>10</NumOfPacGS24>
      </PACGS2>
    </GOOITEGDS>
    <GOOITEGDS>
      <IteNumGDS7>2</IteNumGDS7>
      <GooDesGDS23>Daffodils</GooDesGDS23>
      <GroMasGDS46>1000</GroMasGDS46>
      <NetMasGDS48>950</NetMasGDS48>
      <RESOFCONROC><ConIndROC1>OR</ConIndROC1>
      </RESOFCONROC>
      <PACGS2>
        <MarNumOfPacGS21>AB234</MarNumOfPacGS21>
        <KinOfPacGS23>BX</KinOfPacGS23>
        <NumOfPacGS24>10</NumOfPacGS24>
      </PACGS2>
    </GOOITEGDS>
    <GOOITEGDS>
      <IteNumGDS7>3</IteNumGDS7>
      <GooDesGDS23>Daffodils</GooDesGDS23>
      <GroMasGDS46>1000</GroMasGDS46>
      <NetMasGDS48>950</NetMasGDS48>
      <RESOFCONROC><ConIndROC1>OR</ConIndROC1>
      </RESOFCONROC>
      <PACGS2>
        <MarNumOfPacGS21>AB234</MarNumOfPacGS21>
        <KinOfPacGS23>BX</KinOfPacGS23>
        <NumOfPacGS24>10</NumOfPacGS24>
      </PACGS2>
    </GOOITEGDS>
  </CC051B>

  lazy val CC029B = <CC029B>
    <SynIdeMES1>UNOC</SynIdeMES1>
    <SynVerNumMES2>3</SynVerNumMES2>
    <MesSenMES3>NTA.GB</MesSenMES3>
    <MesRecMES6>alphabetacharliedelta</MesRecMES6>
    <DatOfPreMES9>20210630</DatOfPreMES9>
    <TimOfPreMES10>1319</TimOfPreMES10>
    <IntConRefMES11>alphabeta</IntConRefMES11>
    <AppRefMES14>NCTS</AppRefMES14>
    <TesIndMES18>0</TesIndMES18>
    <MesIdeMES19>alpha</MesIdeMES19>
    <MesTypMES20>GB029B</MesTypMES20>
    <HEAHEA>
      <RefNumHEA4>alphabetacharlie</RefNumHEA4>
      <DocNumHEA5>alphabetacharlie</DocNumHEA5>
      <TypOfDecHEA24>T1</TypOfDecHEA24>
      <CouOfDesCodHEA30>IT</CouOfDesCodHEA30>
      <CouOfDisCodHEA55>GB</CouOfDisCodHEA55>
      <IdeOfMeaOfTraAtDHEA78>NC15 REG</IdeOfMeaOfTraAtDHEA78>
      <NatOfMeaOfTraAtDHEA80>GB</NatOfMeaOfTraAtDHEA80>
      <ConIndHEA96>0</ConIndHEA96>
      <NCTRetCopHEA104>0</NCTRetCopHEA104>
      <AccDatHEA158>20201028</AccDatHEA158>
      <IssDatHEA186>20201028</IssDatHEA186>
      <DiaLanIndAtDepHEA254>EN</DiaLanIndAtDepHEA254>
      <NCTSAccDocHEA601LNG>EN</NCTSAccDocHEA601LNG>
      <TotNumOfIteHEA305>1</TotNumOfIteHEA305>
      <TotNumOfPacHEA306>10</TotNumOfPacHEA306>
      <TotGroMasHEA307>1000</TotGroMasHEA307>
      <BinItiHEA246>0</BinItiHEA246>
      <AutIdHEA380>GB-AUTH-42</AutIdHEA380>
      <DecDatHEA383>20210630</DecDatHEA383>
      <DecPlaHEA394>Dover</DecPlaHEA394>
    </HEAHEA>
    <TRAPRIPC1>
      <NamPC17>NCTS UK TEST LAB HMCE</NamPC17>
      <StrAndNumPC122>11TH FLOOR, ALEX HOUSE, VICTORIA AV</StrAndNumPC122>
      <PosCodPC123>SS99 1AA</PosCodPC123>
      <CitPC124>SOUTHEND-ON-SEA, ESSEX</CitPC124>
      <CouPC125>GB</CouPC125>
      <TINPC159>GB954131533000</TINPC159>
    </TRAPRIPC1>
    <TRACONCO1>
      <NamCO17>NCTS UK TEST LAB HMCE</NamCO17>
      <StrAndNumCO122>11TH FLOOR, ALEX HOUSE, VICTORIA AV</StrAndNumCO122>
      <PosCodCO123>SS99 1AA</PosCodCO123>
      <CitCO124>SOUTHEND-ON-SEA, ESSEX</CitCO124>
      <CouCO125>GB</CouCO125>
      <TINCO159>GB954131533000</TINCO159>
    </TRACONCO1>
    <TRACONCE1>
      <NamCE17>NCTS UK TEST LAB HMCE</NamCE17>
      <StrAndNumCE122>ITALIAN OFFICE</StrAndNumCE122>
      <PosCodCE123>IT99 1IT</PosCodCE123>
      <CitCE124>MILAN</CitCE124>
      <CouCE125>IT</CouCE125>
      <TINCE159>IT11ITALIANC11</TINCE159>
    </TRACONCE1>
    <CUSOFFDEPEPT>
      <RefNumEPT1>GB000060</RefNumEPT1>
    </CUSOFFDEPEPT>
    <CUSOFFTRARNS>
      <RefNumRNS1>FR001260</RefNumRNS1>
      <ArrTimTRACUS085>20210630</ArrTimTRACUS085>
    </CUSOFFTRARNS>
    <CUSOFFDESEST>
      <RefNumEST1>IT018100</RefNumEST1>
    </CUSOFFDESEST>
    <CUSOFFRETCOPOCP>
      <RefNumOCP1>GB000001</RefNumOCP1>
      <CusOffNamOCP2>Central Community Transit Office</CusOffNamOCP2>
      <StrAndNumOCP3>BT-CCTO, HM Revenue and Customs</StrAndNumOCP3>
      <CouOCP4>GB</CouOCP4>
      <PosCodOCP6>BX9 1EH</PosCodOCP6>
      <CitOCP7>SALFORD</CitOCP7>
    </CUSOFFRETCOPOCP>
    <CONRESERS>
      <ConDatERS14>20210630</ConDatERS14>
      <ConResCodERS16>A3</ConResCodERS16>
      <ConByERS18>Not Controlled</ConByERS18>
      <DatLimERS69>20210630</DatLimERS69>
    </CONRESERS>
    <SEAINFSLI>
      <SeaNumSLI2>1</SeaNumSLI2>
      <SEAIDSID>
        <SeaIdeSID1>NCTS001</SeaIdeSID1>
      </SEAIDSID>
    </SEAINFSLI>
    <GUAGUA>
      <GuaTypGUA1>1</GuaTypGUA1>
      <GUAREFREF>
        <GuaRefNumGRNREF1>12GBalphabetachar</GuaRefNumGRNREF1>
        <AccCodREF6>AC01</AccCodREF6>
        <VALLIMECVLE>
          <NotValForECVLE1>0</NotValForECVLE1>
        </VALLIMECVLE>
      </GUAREFREF>
    </GUAGUA>
    <GOOITEGDS>
      <IteNumGDS7>1</IteNumGDS7>
      <GooDesGDS23>Daffodils</GooDesGDS23>
      <GroMasGDS46>1000</GroMasGDS46>
      <NetMasGDS48>950</NetMasGDS48>
      <SPEMENMT2>
        <AddInfMT21>20.22EUR12GBalphabetachar</AddInfMT21>
        <AddInfCodMT23>CAL</AddInfCodMT23>
      </SPEMENMT2>
      <PACGS2>
        <MarNumOfPacGS21>AB234</MarNumOfPacGS21>
        <KinOfPacGS23>BX</KinOfPacGS23>
        <NumOfPacGS24>10</NumOfPacGS24>
      </PACGS2>
    </GOOITEGDS>
  </CC029B>

  lazy val CC060A =
    <CC060A>
      <SynIdeMES1>UNOC</SynIdeMES1>
      <SynVerNumMES2>3</SynVerNumMES2>
      <MesSenMES3>NTA.GB</MesSenMES3>
      <MesRecMES6>alphabetacharliedelta</MesRecMES6>
      <DatOfPreMES9>20210630</DatOfPreMES9>
      <TimOfPreMES10>1319</TimOfPreMES10>
      <IntConRefMES11>alphabeta</IntConRefMES11>
      <MesIdeMES19>alpha</MesIdeMES19>
      <MesTypMES20>GB060A</MesTypMES20>
      <HEAHEA>
        <DocNumHEA5>alphabetacharlie</DocNumHEA5>
        <DatOfConNotHEA148>12345678</DatOfConNotHEA148>
      </HEAHEA>
      <TRAPRIPC1>
        <NamPC17>alphabetacharliedelta</NamPC17>
        <StrAndNumPC122>alphabetacharliedelta</StrAndNumPC122>
        <PosCodPC123>alphabeta</PosCodPC123>
        <CitPC124>alphabetacharliedelta</CitPC124>
        <CouPC125>ZV</CouPC125>
      </TRAPRIPC1>
      <CUSOFFDEPEPT>
        <RefNumEPT1>alphabet</RefNumEPT1>
      </CUSOFFDEPEPT>
    </CC060A>

  lazy val CC009A = <CC009A>
    <SynIdeMES1>UNOC</SynIdeMES1>
    <SynVerNumMES2>3</SynVerNumMES2>
    <MesSenMES3>NTA.GB</MesSenMES3>
    <MesRecMES6>alphabetacharliedelta</MesRecMES6>
    <DatOfPreMES9>20210630</DatOfPreMES9>
    <TimOfPreMES10>1319</TimOfPreMES10>
    <IntConRefMES11>alphabeta</IntConRefMES11>
    <MesIdeMES19>alpha</MesIdeMES19>
    <MesTypMES20>GB060A</MesTypMES20>
    <HEAHEA>
      <DocNumHEA5>alphabetacharlie</DocNumHEA5>
      <CanIniByCusHEA94>1</CanIniByCusHEA94>
      <DatOfCanDecHEA146>20210630</DatOfCanDecHEA146>
    </HEAHEA>
    <TRAPRIPC1/>
    <CUSOFFDEPEPT>
      <RefNumEPT1>alphabet</RefNumEPT1>
    </CUSOFFDEPEPT>
  </CC009A>

}
