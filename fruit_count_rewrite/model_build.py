from model_utils import *
from torchvision import models
import torch.nn.functional as F


class Res101_SFCN(nn.Module):
    def __init__(self, pretrained=True):
        super(Res101_SFCN, self).__init__()
        self.seen = 0
        self.backend_feat = [512, 512, 512, 256, 128, 64]
        self.frontend = []

        self.backend = make_layers(self.backend_feat, in_channels=1024, dilation=True)
        self.convDU = convDU(in_out_channels=64, kernel_size=(1, 9))
        self.convLR = convLR(in_out_channels=64, kernel_size=(9, 1))

        self.output_layer = nn.Sequential(nn.Conv2d(64, 1, kernel_size=1), nn.ReLU())

        initialize_weights(self.modules())

        res = models.resnet101(pretrained=pretrained)
        # pre_wts = torch.load(model_path)
        # res.load_state_dict(pre_wts)
        self.frontend = nn.Sequential(
            res.conv1, res.bn1, res.relu, res.maxpool, res.layer1, res.layer2
        )
        self.own_reslayer_3 = make_res_layer(Bottleneck, 256, 23, stride=1)
        self.own_reslayer_3.load_state_dict(res.layer3.state_dict())

    def forward(self, x):
        x = self.frontend(x)

        x = self.own_reslayer_3(x)

        # pdb.set_trace()
        x = self.backend(x)
        x = self.convDU(x)
        x = self.convLR(x)
        x = self.output_layer(x)

        x = F.upsample(x, scale_factor=8)
        return x
